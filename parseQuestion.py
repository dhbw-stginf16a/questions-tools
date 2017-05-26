#!/usr/bin/python
import json
import glob, os
import sys, getopt

def generateJSON(categories):
    # Undo former chdir
    #os.chdir("..")
    # TODO: Rework versioning
    version = 0.1
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
        categoryName = category[0]["category"]
        data["categories"][categoryName] = category
    jsonData = json.dumps(data)
    exportFile.write(jsonData)
    exportFile.close()

def generatePDF(questions):
    pass

#path = os.getcwd()
#path += "/questions"
#os.chdir(path)
categories = []
for directory in glob.glob("questions/*"):
    print(directory)
    questions = []
    for question in glob.glob(directory + "/*.json"):
        print(questions)
        with open(question, encoding='utf-8') as data_file:
            data = json.loads(data_file.read())
            questions.append(data)
    categories.append(questions)

# Save approved questions and print all questions for verification
approvedCategories = []
for questions in categories:
    approvedQuestions = []
    for question in questions:
        if (question["status"] == "approved"):
            approvedQuestions.append(question)
            print("Kategory: " +  question["category"])
            print("Question: " + question["question"] + "\n \n")

            if (len(question["possibilities"]) > 0):
                for possibility in question["possibilities"]:
                    print(possibility["index"] + ": " + possibility["text"] + "\n")

                    for answer in question["answers"]:
                        print("Answer: " + answer["text"])
                        print("##############################################")
    approvedCategories.append(approvedQuestions)

if(len(sys.argv) > 1):
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
