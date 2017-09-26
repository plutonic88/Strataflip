package cyberpsycho;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Strataflip {
	
	public static HashMap<String, HashMap<String, String>> assignedgames = new HashMap<String, HashMap<String, String>>();
	public static HashMap<Integer, String> assignedgames_feature = new HashMap<Integer, String>();
	
	public static HashMap<String, ArrayList<String>> survey_answers = new HashMap<String,  ArrayList<String>>();
	public static HashMap<Integer, String> survey_answers_feature = new HashMap<Integer, String>();
	
	
	public static HashMap<String, ArrayList<String>> game_history = new HashMap<String,  ArrayList<String>>();
	public static HashMap<Integer, String> game_history_feature = new HashMap<Integer, String>();
	
	
	
	public static int[] answer_score = {5,4,3,2,1};
	
	
	public Strataflip()
	{
		
	}
	
	
	public static void loadgamehistory()
	{
		File csvtrainData = new File("/Users/anjonsunny/Desktop/data/game_history.csv");
		int i = 0; 
		int j = 0;


		try 
		{
			CSVParser parser = CSVParser.parse(csvtrainData, StandardCharsets.US_ASCII, CSVFormat.EXCEL);
			for (CSVRecord csvRecord : parser) 
			{
				Iterator<String> itr = csvRecord.iterator();
				j=0;
				 ArrayList<String> row = new  ArrayList<String>();
				while(itr.hasNext())
				{
					if(i==0)
					{
						game_history_feature.put(j, itr.next());
					}
					else
					{
						row.add(itr.next());
					}
					j++;
					

				}
				if(i>0)
				{
					game_history.put(row.get(0), row);
				}
				i++;
				/*if(i == 18)  // parse data upto a limit
					break;*/
			}
			System.out.println("game history loaded");

		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	
	
	public static void loadassignedgames()
	{
		File csvtrainData = new File("/Users/anjonsunny/Desktop/data/assignedgames.csv");
		int i = 0; 
		int j = 0;


		try 
		{
			CSVParser parser = CSVParser.parse(csvtrainData, StandardCharsets.US_ASCII, CSVFormat.EXCEL);
			for (CSVRecord csvRecord : parser) 
			{
				Iterator<String> itr = csvRecord.iterator();
				j=0;
				HashMap<String, String> row = new HashMap<String, String>();
				while(itr.hasNext())
				{
					if(i==0)
					{
						assignedgames_feature.put(j, itr.next());
					}
					else
					{
						row.put(assignedgames_feature.get(j), itr.next());
					}
					j++;
					

				}
				if(i>0)
				{
					assignedgames.put(row.get("id"), row);
				}
				i++;
				/*if(i == 18)  // parse data upto a limit
					break;*/
			}

		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	public static void loadsurveyanswers()
	{
		File csvtrainData = new File("/Users/anjonsunny/Desktop/data/survey_answers.csv");
		int i = 0; 
		int j = 0;


		try 
		{
			CSVParser parser = CSVParser.parse(csvtrainData, StandardCharsets.US_ASCII, CSVFormat.EXCEL);
			for (CSVRecord csvRecord : parser) 
			{
				Iterator<String> itr = csvRecord.iterator();
				j=0;
				 ArrayList<String> row = new  ArrayList<String>();
				while(itr.hasNext())
				{
					if(i==0)
					{
						survey_answers_feature.put(j, itr.next());
					}
					else
					{
						row.add(itr.next());
					}
					j++;
					

				}
				if(i>0)
				{
					survey_answers.put(row.get(0), row);
				}
				i++;
				/*if(i == 18)  // parse data upto a limit
					break;*/
			}

		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public static void computeDarkTriad() {
		
		HashMap<Integer, String> users = usersPlayedGames();
		
		
		
		for(Integer u: users.keySet())
		{
			System.out.println("user_"+ u +" : "+ users.get(u));
		}
		
		
		HashMap<String, double[]> darktriadscore = new HashMap<String, double[]>();
		
		
		
		// 0
		
		
		for(String user_id: users.values())
		{
			double[] score = new double[3]; //machiavellianism, narcissism, psychopathy
			
			
			// get the answer row
			
			for( ArrayList<String> row : survey_answers.values())
			{
				
				if(row.get(1).equals(user_id))
				{
					//System.out.println("user id "+ user_id );
					double sum = 0;
					int scorecounter = 0;
					for(int q=0; q<27; q++)
					{
						if((q+1)%10 == 0)
						{
							sum = 0;
						}
						
						//String key = "Question_"+(q+1);
						String val= row.get(q+2);
						if(!val.equals("NULL"))
						{
							sum +=  answer_score[Integer.parseInt(val)-1];
							//System.out.println("adding "+ answer_score[Integer.parseInt(val)-1]);
						}
						if(q%9==8)
						{
							score[scorecounter++] = sum/9.0;
						}
						
					}
					System.out.println("mac "+ new DecimalFormat("#0.00").format(score[0])  + ", nar "+ new DecimalFormat("#0.00").format(score[1]) +
							", psy "+ new DecimalFormat("#0.00").format(score[2]) );
					darktriadscore.put(user_id, score);
					
					

				}
				
				
			}
			
			
			
		}
		
		System.out.println("Y");
		
		/*for(String uid: darktriadscore.keySet())
		{
			double da[] = darktriadscore.get(uid);
			for(int j=0; j<3; j++)
				System.out.print( new DecimalFormat("#0.00").format(da[j]) + " ");
			System.out.println();
		}
		*/
		
		HashMap<String, Integer> userpoints = userPoints(assignedgames);
		
		
		ArrayList<String> sorted_mac = sortUsersDarkTriad(darktriadscore,1); // 0 -> mac
		
		System.out.println("\nSorted according to mac ");
		
		for(String uid: sorted_mac)
		{
			double da[] = darktriadscore.get(uid);
			
			for(int j=0; j<3; j++)
				System.out.print( new DecimalFormat("#0.00").format(da[j]) + " ");
			System.out.print(" score "+ userpoints.get(uid));
			System.out.println();
		}
		
		
		
		System.out.println("\nSorted according to nar ");
		ArrayList<String> sorted_nar = sortUsersDarkTriad(darktriadscore,2); // 0 -> mac
		
		
		for(String uid: sorted_nar)
		{
			double da[] = darktriadscore.get(uid);
			for(int j=0; j<3; j++)
				System.out.print( new DecimalFormat("#0.00").format(da[j]) + " ");
			System.out.print(" score "+ userpoints.get(uid));
			System.out.println();
		}
		
		
		
		
		System.out.println("\nSorted according to psy ");
		ArrayList<String> sorted_psy = sortUsersDarkTriad(darktriadscore,3); // 0 -> mac
		
		
		for(String uid: sorted_psy)
		{
			double da[] = darktriadscore.get(uid);
			for(int j=0; j<3; j++)
				System.out.print( new DecimalFormat("#0.00").format(da[j]) + " ");
			System.out.print(" score "+ userpoints.get(uid));
			System.out.println();
		}
		
		System.out.println("\nSorted according to score ");
		ArrayList<String> sorted_score = sortUsersScore(userpoints); // 0 -> score
		
		
		for(String uid: sorted_score)
		{
			double da[] = darktriadscore.get(uid);
			for(int j=0; j<3; j++)
				System.out.print( new DecimalFormat("#0.00").format(da[j]) + " ");
			System.out.print(" score "+ userpoints.get(uid));
			System.out.println();
		}
		
		
		
		int gametype = 1;
		int order = 0;
		
		ArrayList<String> user_fullinfo_rand_to_max = getUser(assignedgames, gametype, order);
		
		
		System.out.println("\nUsers for fullinfo rand to max def ");
		for(String uid: user_fullinfo_rand_to_max)
		{
			double da[] = darktriadscore.get(uid);
			System.out.print(uid + " ");
			for(int j=0; j<3; j++)
				System.out.print( new DecimalFormat("#0.00").format(da[j]) + " ");
			System.out.print(" score "+ userpoints.get(uid));
			System.out.println();
		}
		
		
		
		
		
		
		
		
		
	}
	
	private static ArrayList<String> getUser(HashMap<String, HashMap<String, String>> assignedgames2, int gametype, int order) {
		
		ArrayList<String> user = new ArrayList<String>();
		
		
		
		for(HashMap<String, String> p: assignedgames2.values())
		{
			if((Integer.parseInt(p.get("total_point"))>50) && 
					(Integer.parseInt(p.get("game_type")) == gametype) &&
					(Integer.parseInt(p.get("pick_def_order"))==order))
			{
					user.add(p.get("user_id"));
			}
		}
		
		
		
		return user;
	}


	private static ArrayList<String> sortUsersScore(HashMap<String, Integer> userpoints) {
		

		ArrayList<String> sorted_score = new ArrayList<String>();
		
		
		//ArrayList<String> sorteduser = new ArrayList<String>();
		double[][] data = new double[userpoints.size()][2];
		String[] ids = new String[data.length];
		
		
		int index = 0;
		for(String id: userpoints.keySet())
		{
			int d = userpoints.get(id);
			data[index][0] = index;
			data[index][1] = d;
			ids[index] = id;
			index++;
		}
		
		
		sortArrayDesc(data, ids, 1);
		
		
		
		for(int in=0; in<data.length; in++)
		{
			/*for(int j=0; j<4; j++)
				System.out.print( new DecimalFormat("#0.00").format(data[in][j]) + " ");
			System.out.println();*/
			sorted_score.add(ids[in]);
		}
		
		return sorted_score;
	}


	private static HashMap<String, Integer> userPoints(HashMap<String, HashMap<String, String>> assignedgames2) {
		// TODO Auto-generated method stub
		
		
		HashMap<String, Integer> points = new HashMap<String, Integer>();
		
		
		for(HashMap<String, String> p: assignedgames2.values())
		{
			if(Integer.parseInt(p.get("total_point"))>200)
					points.put(p.get("user_id"), Integer.parseInt(p.get("total_point")));
		}
		
		
		return points;
	}


	private static void sortArrayDesc(double[][] ds, String[] ids, int ind)
	{

		double[] swap = {0.0,0.0,0.0,0.0};
		String tmp = "";

		for (int i = 0; i < ds.length; i++) 
		{
			for (int d = 1; d < ds.length-i; d++) 
			{
				if (ds[d-1][ind] < ds[d][ind]) /* For descending order use < */
				{
					
					
					swap = ds[d];
					ds[d]  = ds[d-1];
					ds[d-1] = swap;
					
					
					
					
					tmp = ids[d];
					ids[d] = ids[d-1];
					ids[d-1] = tmp;
				}
			}
		}

	}


	private static ArrayList<String> sortUsersDarkTriad(HashMap<String, double[]> darktriadscore, int i) {
		
		
		
		/*for(String uid: darktriadscore.keySet())
		{
			double da[] = darktriadscore.get(uid);
			for(int j=0; j<3; j++)
				System.out.print( new DecimalFormat("#0.00").format(da[j]) + " ");
			System.out.println();
		}*/
		
		
		
		ArrayList<String> sorteduser = new ArrayList<String>();
		double[][] data = new double[darktriadscore.size()][4];
		String[] ids = new String[data.length];
		
		
		int index = 0;
		for(String id: darktriadscore.keySet())
		{
			double d[] = darktriadscore.get(id);
			ids[index] = id;
			
			
			data[index][0] = index;
			int indj = 1;
			
			for(double v: d)
			{
				data[index][indj++] = v;
				
			}
			index++;
		}
		
		
		sortArrayDesc(data, ids, i);
		
		for(int in=0; in<data.length; in++)
		{
			/*for(int j=0; j<4; j++)
				System.out.print( new DecimalFormat("#0.00").format(data[in][j]) + " ");
			System.out.println();*/
			sorteduser.add(ids[in]);
		}
		
		return sorteduser;
		
	
	}


	private static HashMap<Integer, String> usersPlayedGames() {
		
		HashMap<Integer, String> users  = new HashMap<Integer, String>();
		
		// from the assigned games table find users
		int i=0;
		for(HashMap<String, String> row: Strataflip.assignedgames.values())
		{
			if(Integer.parseInt(row.get("total_point"))> 200)
				users.put(i++, row.get("user_id"));
		}
		
		
		return users;
		
		
	}
	

}
