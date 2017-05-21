import json
import glob, os

path = os.getcwd()
path += "/questions"
os.chdir(path)
questions = []
for question in glob.glob("*.json"):
    with open(question, encoding='utf-8') as data_file:
        data = json.loads(data_file.read())
        questions.append(data)

for question in questions:
    print("Question: " + question["question"] + "\n \n")

    if (len(question["possibilities"]) > 0):
        for possibility in question["possibilities"]:
            print(possibility["index"] + ": " + possibility["text"] + "\n")

    for answer in question["answer"]:
        print("Answer: " + answer["text"])
    print("##############################################")
