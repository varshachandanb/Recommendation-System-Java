

import java.io.*;
import java.util.*;
import java.lang.*;
public class Recommendation 
        
{
    public void findPairs(String user,ArrayList userRatings,HashMap ratingData,ArrayList movieList,ArrayList userRatedItems)
    {
        //find the combination of all movies
        //join them using "$" and for every combination of movies find the common users who hav rated it
        
        ArrayList<String>movieComb=new ArrayList<String>();
        HashMap<String,Double> pairWeight= new HashMap<String,Double>();
        ArrayList<String> unratedMovies= new ArrayList<String>();
        HashMap<String,Double> tempRatings= new HashMap<String,Double> ();
        HashMap<String,Double> predRating=new HashMap<String,Double> ();
        
       
        for (int i=0;i<movieList.size()-1;i++)
        {
            for (int j=i+1;j<movieList.size();j++)
            {                
                movieComb.add(movieList.get(i).toString());
                movieComb.add(movieList.get(j).toString());
            }
        }
       
       
        
        for (int k=0;k<movieComb.size();k+=2)
        {
            int coratedUsers=0;
            double sum1=0.0;
            double sum2=0.0;
            double result=0.0;
            double denom=0.0;
            double total1=0.0;
            double total2=0.0;  
            double total3=0.0;  
            double avg1=0.0;
            double avg2=0.0;
            ArrayList<String> value1= new ArrayList<String>();
            ArrayList<String> value2=new ArrayList<String>() ;
            String movie1=movieComb.get(k);
            String movie2=movieComb.get(k+1);   
            if(ratingData.containsKey(movie1))
             value1= (ArrayList)ratingData.get(movie1);
            
            if(ratingData.containsKey(movie2))
             value2= (ArrayList)ratingData.get(movie2);
            
            ArrayList<String> userList1= new ArrayList<String>();
            ArrayList<String> userList2= new ArrayList<String>();
           
                for(int l=0;l<value1.size();l+=2)
                {
                    String user1=value1.get(l);
                    for(int m=0;m<value2.size();m+=2)
                    {
                        String user2=value2.get(m);
                        if(user1.equals(user2))
                        {
                            coratedUsers++;
                            userList1.add(user1);
                            userList1.add(value1.get(l+1));
                            userList2.add(user2);
                            userList2.add(value2.get(m+1)); 
                        }
                    }             
                }
                
                for (int p=1;p<userList1.size();p+=2)
                {
                 sum1=sum1+Double.parseDouble(userList1.get(p));
                 
                }
                for (int p=1;p<userList2.size();p+=2)
                {
                 sum2=sum2+Double.parseDouble(userList2.get(p));
                
                }
                if(coratedUsers==0)
                {
                    avg1=0.0;
                    avg2=0.0;
                }
                else
                {
                    avg1=(double)sum1/(double)coratedUsers;
                    avg2=(double)sum2/(double)coratedUsers;                   
                }
                int counter=1;
                for(int q=0;q<coratedUsers;q++)
                {
                    total1+=((Double.parseDouble(userList1.get(counter))+avg1)*((Double.parseDouble(userList2.get(counter))+avg2)));
                    total2=total2+java.lang.Math.pow(((Double.parseDouble(userList1.get(counter)))+avg1),2);
                    total3=total3+java.lang.Math.pow(((Double.parseDouble(userList2.get(counter)))+avg2),2);
                    counter+=2;
                }
                denom=java.lang.Math.sqrt(total2)*java.lang.Math.sqrt(total3);
                if(denom==0.0)
                {
                    result=0.0;
                }
                else
                {
                    result=total1/denom;
                }
                String movies=movie1+"$"+movie2;
                pairWeight.put(movies,result);
           
        }
     
        for (int m=0;m< userRatings.size();m+=2)
        {
            tempRatings.put((String)userRatings.get(m),Double.parseDouble((String) userRatings.get(m+1)));
        }
        
       
        //find unrated movies
        for (int m=0;m<movieList.size();m++)
        {
        	
        	boolean val=userRatedItems.contains(movieList.get(m));
            if(!val)
            {
               unratedMovies.add(movieList.get(m).toString());
            }
        }
        
        
       
        for (int x=0;x<unratedMovies.size();x++)
        {
            double total=0.0;
            double denom=0.0;
            for (int y =0;y<userRatings.size();y+=2) 
            {
                String pairs1=unratedMovies.get(x)+"$"+userRatings.get(y);
                String pairs2=userRatings.get(y)+"$"+unratedMovies.get(x);
                
                
                if(pairWeight.containsKey(pairs1))
                {
                	tempRatings.put(pairs1,pairWeight.get(pairs1));
                	
                }
                else if (pairWeight.containsKey(pairs2))
                {
                	tempRatings.put(pairs2,pairWeight.get(pairs2));
                	
                }
                
            }
            
           
            for (String key : tempRatings.keySet()) 
            {
                for (String pair: pairWeight.keySet())
                {
                    if(pair.equals(key))
                    {
                        double val =pairWeight.get(pair).doubleValue();
                        total+=(double)(tempRatings.get(key))*val;  
                        denom+=Math.abs(val);
                    }
                }

            }
            if(total==0.0)
            {
                predRating.put(userRatings.get(x).toString(),0.0);
            }
            else
            {
               double ans=total/denom;
               predRating.put(userRatings.get(x).toString(),ans);
            }
        
        }
        //print all predictions
        System.out.println("Rating Predictions for "+user);
        for (String key : predRating.keySet()) 
        {
            System.out.println(predRating.get(key));
            
        }
       
 }


  public static void main(String[] args) throws IOException
    {
	  ArrayList<String> userRatings= new ArrayList<String>();
      HashMap<String,ArrayList<String>>ratingData = new HashMap<String, ArrayList<String>>();
      ArrayList<String> movieList=new ArrayList<String>();
      ArrayList<String>userRatedItems=new ArrayList<String>();
	  String line = null;
      String[] word= new String[3];
      String user="mongoose";        
        try
        {
        File file = new File("D:/MS/Projects/Eclipse/Reccomendation System/src/ratings-dataset.txt");        
        FileInputStream fis = new FileInputStream(file); 
        //Construct BufferedReader from InputStreamReader
       
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));        
       
        while ((line = br.readLine()) != null) {
		word=line.split("\t");
		//0-> user 1-> rating 2-> movieName
                boolean movieName=ratingData.containsKey(word[2]);                
                if(movieName)
                {
                    ArrayList<String> arr=ratingData.get(word[2]);
                    arr.add(word[0]);
                    arr.add(word[1]);
                    ratingData.put(word[2],arr);
                }
                else
                {
                    ArrayList<String> arr= new ArrayList<String>();
                	arr.add(word[0]);
                    arr.add(word[1]); 
                    ratingData.put(word[2],arr);
                } 
                if (word[0].equals(user))
                {
                	// list with rating  and their corresponding movies of this user
                    userRatings.add(word[2]); //movie
                    userRatings.add(word[1]); //rating
                    userRatedItems.add(word[2]);
                }
                if(!movieList.contains(word[2]))
                    movieList.add(word[2]);
                }
	    br.close(); 
        }
       
        catch(Exception e)
        {
        System.out.println(e.getMessage());
        
        }
        Recommendation re = new Recommendation();
        re.findPairs(user,userRatings,ratingData,movieList,userRatedItems);
    }
    
} 

