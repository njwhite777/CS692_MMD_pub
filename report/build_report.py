#!/usr/bin/env python3

from __future__ import print_function
import pickle
import os.path
from googleapiclient.discovery import build
from google_auth_oauthlib.flow import InstalledAppFlow
from google.auth.transport.requests import Request
import glob
from creds import *
import sys
import time

# If modifying these scopes, delete the file token.pickle.
#
# # The ID of a sample document.
#
def makeNewText(text="\n"):
    return {
             'insertText': {
                 'location': {
                     'index': 1,
                 },
                 'text': text
             }
         }

def main():
    """Shows basic usage of the Docs API.
    Prints the title of a sample document.
    """
    creds = None
    # The file token.pickle stores the user's access and refresh tokens, and is
    # created automatically when the authorization flow completes for the first
    # time.
    if os.path.exists('token.pickle'):
        with open('token.pickle', 'rb') as token:
            creds = pickle.load(token)
    # If there are no (valid) credentials available, let the user log in.
    if not creds or not creds.valid:
        if creds and creds.expired and creds.refresh_token:
            creds.refresh(Request())
        else:
            flow = InstalledAppFlow.from_client_secrets_file(
                'credentials.json', SCOPES)
            creds = flow.run_local_server()
        # Save the credentials for the next run
        with open('token.pickle', 'wb') as token:
            pickle.dump(creds, token)

    service = build('docs', 'v1', credentials=creds)

    files = glob.glob('../test/*')
    fileNames = dict()
    for f in files:
        fileNames[os.path.basename(f)] = dict()

    updates = list()
    blackBoxTestCases = list()
    blackBoxTestCases.append(makeNewText("Black Box Testing\n"))
    blackBoxTestCases.append(makeNewText())
    
    for idx,f in enumerate(files):
        currentName = os.path.basename(f)
        inputPath   = os.path.join(f,'input')
        outputPath  = os.path.join(f,'output')
        print("Setting up report for {}. Complete {}/{} tests".format(currentName,idx+1,len(files)))
        
        isErrorCase = False
        if(os.path.exists(os.path.join(outputPath,"error.txt"))):
            isErrorCase=True
        
        blackBoxTestCases.append(makeNewText("\n======================================\nTest Case for {}\n\n----------------INPUTS----------------\n".format(currentName)))
        
        buildInputFormatRequestsString=""
        with open(os.path.join(inputPath,"{}.dma_fmt".format(currentName)),'r') as hf:
            buildInputFormatRequestsString+="FORMAT FILE: {}.dma_fmt\n".format(currentName)
            rows = list()
            for row in hf:
                rows.append(row)
            if(len(rows) < 20):
                for row in rows:
                    buildInputFormatRequestsString+="{}\n".format(row.strip())
            else:
                buildInputFormatRequestsString+="Format data too long. See header file {}\n".format(os.path.join(inputPath,currentName+".dma_fmt"))
                
        blackBoxTestCases.append(makeNewText(buildInputFormatRequestsString+"\n"))
        
        buildInputRequestsString=""
        with open(os.path.join(inputPath,"{}.dma".format(currentName)),'r') as df:
            buildInputRequestsString+="DATA FILE: {}.dma\n".format(currentName)
            rows = list()
            for row in df:
                rows.append(row)
            if(len(rows) < 20):
                for row in rows:
                    buildInputRequestsString+="{}".format(row)
            else:
                buildInputRequestsString+="Data too long. See data file {}\n".format(os.path.join(inputPath,currentName+".dma"))
            
        blackBoxTestCases.append(makeNewText(buildInputRequestsString))
        
        blackBoxTestCases.append(makeNewText("\n----------------OUTPUTS---------------\n".format(currentName)))
        
        buildErrorCaseString = ""
        if(not(isErrorCase)):
            buildErrorCaseString += "XML OUTPUT: \n"
            lines = 40
            
            with open(os.path.join(outputPath,"moduleData.xml"),'r') as hf:
                rows = list()
                for row in hf:
                    rows.append(row)
                if(len(rows) < lines):
                    for row in rows:
                        buildErrorCaseString+=row
                else:
                    buildErrorCaseString+="XML output too long. Cuts off at {}.\nSee {}\n".format(lines,os.path.join(outputPath,"moduleData.xml"))
                    
            buildErrorCaseString+="\nDOT OUTPUT (text):\n"
            with open(os.path.join(outputPath,"moduleData.dot"),'r') as hf:
                rows = list()
                for row in hf:
                    rows.append(row)
                if(len(rows) < 20):
                    for row in rows:
                        buildErrorCaseString+=row
                    buildErrorCaseString+="\n"
                else:
                    buildErrorCaseString+="Dot output too long. See {}\n".format(os.path.join(outputPath,"moduleData.dot"))
        else:
            with open(os.path.join(outputPath,"error.txt"),'r') as hf:
                rows = list()
                for row in hf:
                    rows.insert(0,row)
                if(not(len(rows) > 20)):
                    for row in rows:
                        buildErrorCaseString+=row+"\n"
        blackBoxTestCases.append(makeNewText(buildErrorCaseString))

    print("Writing the report.")
    count=0
    for idx in range(len(blackBoxTestCases)-1,0,-1):
        request = blackBoxTestCases[idx]
        sys.stdout.write("\rProgress {}/{} - {:.2f}% complete.\n".format(len(blackBoxTestCases)-idx,len(blackBoxTestCases), (100*((len(blackBoxTestCases)-idx)/len(blackBoxTestCases)))))
        result = service.documents().batchUpdate(documentId=DOCUMENT_ID, body={'requests': request}).execute()
        if(count==50):
            time.sleep(100)
            count=0
        count+=1
        

if __name__ == '__main__':
    main()
