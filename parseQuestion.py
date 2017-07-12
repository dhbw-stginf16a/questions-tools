#!/usr/bin/python
import json
import glob, os
import sys, getopt

class Tee(object):
    def __init__(self, *files):
        self.files = files
    def write(self, obj):
        for f in self.files:
            f.write(obj)
            f.flush() # If you want the output to be visible immediately
    def flush(self) :
        for f in self.files:
            f.flush()

f = open('QuestionPrint.txt', 'w')
original = sys.stdout
sys.stdout = Tee(sys.stdout, f)

def generateJSON(categories):
    version = "1.0"
    exportFile = ""
    try:
        oldData = open("questions.json", "r")
        oldJSON = json.load(oldData)
        try:
            version = oldJSON["version"]
            version += 0.1
        except Exception as e:
            print("Invalid file, overwriting")
        oldData.close()

        # Remove all data from file
        oldData = open("questions.json", "w")
        oldData.close()
        # Reopen to write new content
        exportFile = open("questions.json", "w")
    except Exception as e:
        print("No file found, will create one for you")
        exportFile = open("questions.json", "w")
    data = {}
    data["version"] = version
    data["categories"] = {}
    for category in categories:
        if (len(category) == 0):
            continue
            # No approved questions
        categoryName = category[0]["category"]
        data["categories"][categoryName] = category
    jsonData = json.dumps(data)
    exportFile.write(jsonData)
    exportFile.close()

def generatePDF(questions):
    pass

categories = []
for directory in glob.glob("questions/*"):
    questions = []
    for question in glob.glob(directory + "/*.json"):
        with open(question, encoding='utf-8') as data_file:
            data = json.loads(data_file.read())
            questions.append(data)
    categories.append(questions)

# Save approved questions and print all questions for verification
approvedCategories = []
# Save warnings about broken questions in draft state
warnings = list()

failed = False
for questions in categories:
    approvedQuestions = []
    for question in questions:
        if (question["status"] == "approved" and question["category"] != "Test"):
            approvedQuestions.append(question)
        print("Category: " +  question["category"])

        # Print a nice name for the question type
        qType = "Type: "

        if (question["type"] == "multipleChoice"):
            qType += "Multiple Choice"

        elif (question["type"] == "fillIn"):
            qType += "Fill the Gap"

        elif (question["type"] == "wildcard"):
            qType += "Free Guess"
        
        else:
            qType += "List"

        print(qType)

        # Print a nice name for the difficulty
        difficulty = "Difficulty: "

        if (question["difficulty"] == 0):
            difficulty += "Easy"

        elif (question["difficulty"] == 1):
            difficulty += "Medium"
        
        else:
            difficulty += "Hard"

        print(difficulty)

        print("Question: " + question["question"] + "\n \n")

        if (len(question["possibilities"]) > 0):
            for possibility in question["possibilities"]:
                print(str(possibility["index"]) + ": " + possibility["text"] + "\n")
        
        try:
             for answer in question["answers"]:
                 print("Answer: " + answer["text"])
        except Exception as e:
            if (question["status"] == "draft"):
                warning = "WARNING: Question '"
                warning += question["question"]
                warning += "' from category '"
                warning += question["category"]
                warning += "' contains invalid answers"
                warnings.append(warning)
                print(warning)
            else:
                print("ERROR")
                print(e)
                failed = True
        
        print("#" * 48, "\n")

    approvedCategories.append(approvedQuestions)

if (len(warnings) > 0):
    for warning in warnings:
        print(warning)
if (failed):
    exit("Some questions in review or approved state have errors.")

if (len(sys.argv) > 1):
    opts, args = getopt.getopt(sys.argv[1:],"hj:p:",["generate-json","generate-pdf"])
    for opt, arg in opts:
      if (opt == '-h'):
         print("To print all questions just run parseQuestions.py")
         print("To generate a single json file with all questions run:")
         print("parseQuestions.py --generate-json")
         print("To generate a ready to print PDF with the questions run:")
         print("parseQuestions.py --generate-pdf")
         sys.exit()
      elif (opt in ("-j", "--generate-json")):
         generateJSON(approvedCategories)
      elif (opt in ("-p", "--generate-pdf")):
         print("Not yet implemented -- exiting")
         sys.exit()
         generatePDF(approvedCategories)
