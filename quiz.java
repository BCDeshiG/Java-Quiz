/* *****************************************************
	AUTHOR: Abhid Islam
		A program that tests the user's knowledge.
		Utilises Arrays, ADT, Bubble Sort and File I/O
   ***************************************************** */

import java.util.Scanner; // Needed to make Scanner available.
import java.lang.*; // Used here to pick out key words.
import java.io.*; // Needed to read file

class quiz
{
	public static void main (String[] param) throws IOException
	{
		int score = 0;
		String qBank = inputString("Please input the location of your questions file.");
		QnA[] qaList = initQA(qBank);
		sortQ(qaList);
		final int quizLength = intro(qaList, qBank);
		for (int i = 0; i < quizLength; i++)
		{
			score = questions(i, qaList, score);
		}
		outro(score, quizLength, qBank);
		System.exit(0);
		
	} // END main

	public static String inputString (String rq) // Used when user input needed
	{
		Scanner sc = new Scanner(System.in);
		System.out.println(rq);
		String ans = sc.nextLine();
		return ans;
	} // END inputString

	public static void sortQ (QnA[] qaList) // Used to sort the questions by difficulty
	{
		boolean sorted = false;
		while (!sorted)
		{
			sorted = true; // Array potentially already sorted
			for (int i=0; i < (qaList.length-1); i++) // Check for unordered pairs
			{
				if (getLVL(qaList[i]) > getLVL(qaList[i+1])) // IF not ordered correctly, swap
				{
					QnA tmp = qaList[i+1];
					qaList[i+1] = qaList[i];
					qaList[i] = tmp;
					sorted = false; // Array wasn't sorted after all
				}
			}
		}
		return;
	} // END sortQ

	public static String[][] readFile (String filename, final int numQuestions) throws IOException
	// Reads contents of file line by line
	{
		BufferedReader inStream = new BufferedReader(new FileReader(filename));
		String nextword = inStream.readLine();
		String[][] dataList = new String[numQuestions][];

		// Skip to start of questions
		nextword = inStream.readLine();
		nextword = inStream.readLine();
		nextword = inStream.readLine();

		int count = 0;
		while (!nextword.contains("#"))
		{
			String[] qData = nextword.split(","); // Split line into array
			dataList[count] = qData; // Put question data in array of questions
			nextword = inStream.readLine(); // Read next line
			count++;
		}

		inStream.close(); // Close file to free it
		return dataList;
	} // END readFile

	public static int getLength (String filename) throws IOException // Gets number of questions from file
	{
		BufferedReader inStream = new BufferedReader(new FileReader(filename));
		String nextword = inStream.readLine();
		nextword = inStream.readLine(); // Skip comment line
		final int numQuestions = Integer.parseInt(nextword);

		inStream.close(); // Close file to free it
		return numQuestions;
	} // END getLength

	public static QnA[] initQA (String qBank) throws IOException // Creates questions and answers
	{
		// Format: Question, lvl, Answer, OppA, OppB, OppC
		final int numQuestions = getLength(qBank);
		String[][] dataList = readFile(qBank, numQuestions);
		QnA[] qaList = new QnA[numQuestions];

		for (int qNum=0; qNum < numQuestions; qNum++) // Adds each question and details to record
		{
			qaList[qNum] = new QnA();
			setQ(qaList[qNum], dataList[qNum][0]);
			setLVL(qaList[qNum], dataList[qNum][1]);
			setAns(qaList[qNum], dataList[qNum][2]);
			setOpp(qaList[qNum], dataList[qNum][3], dataList[qNum][4], dataList[qNum][5]);
		}

		return qaList;
	} // END initQA

	public static int intro (QnA[] qaList, String qBank) throws IOException // Informs user how to play
	{
		System.out.println("\nWelcome to this quiz.");
		System.out.println("You will be asked a number of multiple choice questions.");
		int quizLength = Integer.parseInt(inputString("Please input how many you want to answer (max " + getLength(qBank) + ").")); // Must not exceed length of question list
		if (quizLength > getLength(qBank))
		{
			quizLength = getLength(qBank);
		}
		return quizLength;
	} // END intro

	public static int questions (int num, QnA[] qaList, int score) // Asks question
	{
		System.out.println("\nQuestion " + (num + 1) + ":\n"); // Print question number
		getQ(qaList[num]); // Print Question
		System.out.print("A: " + getOpp(qaList[num], "A")); // Print option A
		System.out.print("    B: " + getOpp(qaList[num], "B")); // Print option B
		System.out.println("    C: " + getOpp(qaList[num], "C")); // Print option C

		boolean hasAnswered = false;
		String answer = "";
		while (!hasAnswered) // Forces user to input valid answer
		{
			answer = inputString("Please input your answer (A/B/C):");
			answer = answer.toUpperCase(); // makes the input upper case so it's not case sensitive
			if (answer.equals("A") || answer.equals("B") || answer.equals("C"))
			{
				hasAnswered = true;
			}
		}
		score = verify(answer, score, qaList, num);

		return score;
	} // END questions

	public static int verify (String a, int score, QnA[] qaList, int num) // Checks their answer is right
	{
		switch (a)
		{
			case "A": // IF they chose A
				if (getOpp(qaList[num], "A").equals(getAns(qaList[num]))) // IF they were correct
				{
					System.out.println("Correct!");
					score++; // Increase their score by 1
					System.out.println("Score: " + score);
					break;
				}
				else
				{
					System.out.println("Incorrect!");
					System.out.println("Score: " + score);
					break;
				}
			case "B": // IF they chose B
				if (getOpp(qaList[num], "B").equals(getAns(qaList[num]))) // IF they were correct
				{
					System.out.println("Correct!");
					score++; // Increase their score by 1
					System.out.println("Score: " + score);
					break;
				}
				else
				{
					System.out.println("Incorrect!");
					System.out.println("Score: " + score);
					break;
				}
			case "C": // IF they chose C
				if (getOpp(qaList[num], "C").equals(getAns(qaList[num]))) // IF they were correct
				{
					System.out.println("Correct!");
					score++; // Increase their score by 1
					System.out.println("Score: " + score);
					break;
				}
				else
				{
					System.out.println("Incorrect!");
					System.out.println("Score: " + score);
					break;
				}
			default: // Shouldn't be run normally
				System.out.println("Error!");
				break;
		}
		return score;
	} // END verify

	public static void outro (int score, int quizLength, String qBank) throws IOException
	// Informs user how well they did
	{
		System.out.println("\nFinal score: " + score); // Prints final score
		if (score == quizLength)
		{
			System.out.println("Perfect!");
			if (quizLength == getLength(qBank))
			{
				String prizeLocation = "prize.txt"; // Not really a prize, just some rudimentary ASCII art
				System.out.println("Find your reward in " + prizeLocation);
				prize(prizeLocation);
			}
		}
		else if (score == (quizLength-1))
		{
			System.out.println("Nearly there! Off by one.");
		}
		else if (score <= (quizLength/2))
		{
			System.out.println("Oof, you need to practise.");
		}
		else
		{
			System.out.println("Better luck next time.");
		}
		return;
	} // END outro

	public static void prize (String out) throws IOException
	// Outputs ASCII Art message to file line by line
	{
        PrintWriter outputStream = new PrintWriter(new FileWriter(out));
        outputStream.println("CCCCC  OOOOO  NN  N  GGGGG  RRRRR  AAAAA  TTTTT  SSSSS");
        outputStream.println("C      O   O  N N N  G      R   R  A   A    T    S");
        outputStream.println("C      O   O  N N N  G  GG  RRRR   AAAAA    T    SSSSS");
        outputStream.println("C      O   O  N  NN  G   G  R  R   A   A    T        S");
        outputStream.println("CCCCC  OOOOO  N   N  GGGGG  R   R  A   A    T    SSSSS");
        outputStream.println("\nYou got a perfect score!");
        outputStream.close(); // Close file to free it
		return;
	} // END prize

/***** GETTERS AND SETTERS *****/

	public static String getQ (QnA q) // Gets question from record and prints to screen
	{
		System.out.println(q.qtn);
		return q.qtn;
	} // END getQ

	public static String getAns (QnA q) // Gets correct answer from record
	{
		return q.correct;
	} // END getAns

	public static String getOpp (QnA q, String opp) // Gets option from record
	{
		if (opp.equals("A"))
		{
			return q.ansA; // IF option A requested, returns option A
		}
		else if (opp.equals("B"))
		{
			return q.ansB; // IF option B requested, returns option B
		}
		else if (opp.equals("C"))
		{
			return q.ansC; // IF option C requested, returns option C
		}
		return "Error";
		// Return needed to avoid compile error, in reality this should never be run.
	} // END getOpp

	public static int getLVL (QnA q) // Gets question difficulty from record
	{
		return q.lvl;
	} // END getLVL

	public static void setQ (QnA q, String question) // Sets question in record
	{
		q.qtn = question;
		return;
	} // END setQ

	public static void setAns (QnA q, String answer) // Sets correct answer in record
	{
		q.correct = answer;
		return;
	} // END setAns

	public static void setOpp (QnA q, String oppA, String oppB, String oppC) // Sets options in record
	{
		q.ansA = oppA; // Sets option A
		q.ansB = oppB; // Sets option B
		q.ansC = oppC; // Sets option C
		return;
	} // END setOpp

	public static void setLVL (QnA q, String difficulty) // Sets difficulty of question in record
	{
		q.lvl = Integer.parseInt(difficulty);
		return;
	} // END setLVL

} // END class quiz

class QnA
{
	String qtn;
	String ansA;
	String ansB;
	String ansC;
	String correct;
	int lvl;
} // END class QnA
