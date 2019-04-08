### Developer Readme

This repo is an Eclipse Maven project, and should be relatively simple to import by following the following path in Eclipse: File->Import->Git->Projects from Git->Next->Clone URI->Next.

Contained in the module are two entry points to execution: the class MMD (see MMD.java) and a "runner" class, MMD_Runner (see MMD_Runner.java) which executes suites test cases using MMD.

#### Developing Test Cases:
To develop test cases, a new entry in the test foulder (./test/) must be made. Note: ./ represents the top level of the Eclipse project. Then create a folder with the name of the test - e.g. dataFileZeroLength/. Inside of this create an input and output directory. Inside of the input directory create a dma_fmt file and a dma (data) file such that the file name reflects the name of the test case directory inside of./test/.  

Alternately, a test can be created using the ./makeTest.sh script:  
```
./makeTest.sh myAwesomeTest
```
This Script automatically builds the test in the expected format.  

Finally, if the test is for an error condition place an error.txt in output. This will cause the error to be properly trapped and handled in the MMD_Runner class. Note that the first line of error.txt should be the exact string of the error that is thrown for the error case under development. 
 
Currently no automation is provided to check a "good" test case, that is a test case which does not cause MMD to throw an error and terminate early. These must be checked by hand.  

###### DEV TLDR:
Create the following directories -  
./test/  
./test/\<testfilename>  
./test/\<testfilename>/input  
./test/\<testfilename>/output  
./test/\<testfilename>/input/\<testfilename>.dma  
./test/\<testfilename>/input/\<testfilename>.dma_fmt  

If you have an error test case -  
./test/\<testfilename>/output/error.txt  

To error.txt write the string of the error that will be generated. For example -  
```sh
"GENERIC EXCEPTION: The input must consist of both a format file (filename.dma_fmt) and a data file (filename.dma)."
```
The above error.txt would contain this example text if somewhere in the program you are generating an:  
```java
throw new Exception("GENERIC EXCEPTION: The input must consist of both a format file (filename.dma_fmt) and a data file (filename.dma).");
```

###### Running:
Open MMD_Runner.java in Eclipse. Press the play button. This should automatically find the test cases in ./test/, build a new MMD object for each input, and run each MMD instance, trapping any errors that may have been produced.

#### Developing the MMD tool:
To develop the MMD tool, work on MMD.java, XmlOut.java, DotOut.java, DATA_DMA.java, DMA_FMT.java, and possibly FileOperations.java.  Follow the specifications

##### Running:
Switch to MMD.java. In order to run this, three command line arguments must be passed. Press run and allow it to run once. Now, press the little black down arrow next to the play button. Click on run configurations. Click on Arguments. Now, provide three arguments to MMD: the path to the test directory, the name of the data and format file (which should also be the name of the test), and the path to the output file (e.g. ./test/\<testfilename>/input \<testfilename> ./test/\<testfilename>/output )

##### Other Notes:
The runner uses colored output to print pretty text in the console window in Eclipse. To achieve this the Ansi plugin must be installed in Eclipse.  
https://marketplace.eclipse.org/content/ansi-escape-console
