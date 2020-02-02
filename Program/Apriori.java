import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.Math;

public class Apriori3 {
	
	static float Min_Confidence=0f;
	static float Min_Support=0f;
	
	public static int eliminate_check(int num,int[] eliminated,int e_size)
	{
		int is_existing=0;
		for(int i=0;i<e_size;i++)
		{
			int num_temp=num;
			int temp=eliminated[i];
			int flag=0;
			while(temp!=0)
			{
				//System.out.println("temp "+temp+" num_temp "+num_temp);
				//System.out.println("temp%10 "+temp%10+"  " +" num_temp "+num_temp%10);
				if(temp%10==1 && num_temp%10==0)
				{
					flag=1;
					break;
				}
				temp=temp/10;
				num_temp=num_temp/10;
			}
			if(flag==0)
			{
				is_existing=1;
				break;
			}
		}
		return is_existing;
	}
	
	
	public static void name_me(int num)
	{
		String[] items= {"Apple","Orange","Lemon","Ginger","Water","Milk","Chips","Diapers","iPhone","Case"};
		int n=0;
		int temp=num;
		while(temp!=0)
		{
			if(temp%10==1)
				n++;
			temp=temp/10;
		}
		
		int[] set=new int[n];
		temp=num;
		int count=0;
		int z=0;
		
		while(temp!=0)
		{
			if(temp%10==1)
			{
				set[z]=count;
				z++;
			}
			count++;
			temp=temp/10;
		}
		for(int i=0;i<n;i++)
		{
			System.out.print(items[set[i]]+" ");
		}
	}
	
	
	public static int  merge(int itemset1,int itemset2)
	{
		String str1=Integer.toString(itemset1);
		String str2=Integer.toString(itemset2);
		//System.out.println(str1+" "+str2);
		int diff=str2.length()-str1.length();
		if(diff>0 )
		{
			for(int i=0;i<diff;i++)
			{
			str1="0"+str1;
			}
		}
		else if(diff<0)
		{
			for(int i=diff;i<0;i++)
				
			{
				str2="0"+str2;
			}
		}
		
		String to_return="";
		for(int i=0;i<str1.length();i++)
		{
			if(str1.charAt(i)=='0' && str2.charAt(i)=='0')
				to_return=to_return+"0";
			else
				to_return=to_return+"1";
		}
		int merged_number=Integer.parseInt(to_return);
		
		//System.out.println(str1+" "+str2+" "+to_return);
		return merged_number;
	}
	
	
	public static int cal_Support(int[][] d,int number)
	{
		int count=0;
		
		for(int i=0;i<20;i++)
		{
			int c=number;
			int flag=1;
			for(int j=0;j<10;j++)
			{
				if(d[i][j]!=c%10 && c%10==1 )
					flag=0;
				c=c/10;
			}
			count=count+flag;
		}
		return count;
	}
	
	
	public static int flip(int n)
	{
		if(n==7)
			return 8;
		else
			return 7;
	}
	
	
	public static void headtail(int n,int num,int d[][])
	{
		int[] set=new int[n];
		int temp=num;
		int count=0;
		int z=0;
		
		while(temp!=0)
		{
			if(temp%10==1)
			{
				set[z]=(int) Math.pow(10,count);
				//System.out.println(set[z]);
				z++;
			}
			count++;
			temp=temp/10;
		}
		
		int c=(int) (Math.pow(2, n));
		int[][] a=new int[c][n];
		for(int j=0;j<n;j++)
		{
			int pq=(int)(Math.pow(2,j+1));
			int bit=7;
			int bc=0;
			for(int i=0;i<c;i++)
			{
				if(bc==c/pq)
				{
					bit=flip(bit);
					bc=0;
				}
				a[i][j]=bit;
				bc++;
			}
		}
			
		int left=0;
		int right=0;
		
		for(int i=1;i<c-1;i++)
		{
			left=0;
			right=0;
			for(int j=0;j<n;j++)
			{
				
				if(a[i][j]==7)
				{
					left=merge(left,set[j]);
				}
				else
				{
					right=merge(right,set[j]);
				}
			}
			//System.out.println("num "+ num+" left "+left+" right "+right);
			float confidence=((float)cal_Support(d,num)/(float)cal_Support(d,left))*100;
			float support=((float)cal_Support(d,num) /20 )*100;
			//System.out.println("confidence "+confidence);
			if(confidence>=Min_Confidence)
			{
				name_me(left);
				System.out.print("->");
				name_me(right);
				System.out.print("["+support+","+confidence+"]\n");
			}
		}
	}
	
	
	public static void associations(int num,int d[][])
	{
		int nOfOne=0;
		int temp=num;
		while(temp!=0)
		{
			if(temp%10==1)
				nOfOne++;
			temp=temp/10;
		}
		//System.out.println(nOfOne);
		//int size=((int) Math.pow(2, nOfOne))-2;
		headtail(nOfOne,num,d);
	}
	
	
	public static void Table_creation(int d1[][])
	{
		//System.out.println("//Table c1");
		//Table c1
		int c1[][]=new int[2][10];
		for(int i=0;i<10;i++)
		{
			c1[0][i]=(int) Math.pow(10,i);
			c1[1][i]=cal_Support(d1,c1[0][i]);
			
		}
		
		//Printing c1
//		for(int i=0;i<10;i++)
//		{
//			System.out.println(c1[0][i]+" "+c1[1][i]);
//		}
		//calculating size for L1
		int size=0; // size for next array -> L1 array
		for(int i=0;i<10;i++)
		{
			if(((float)c1[1][i]/20)*100>=Min_Support)
			{
				//System.out.println(c1[0][i]);
				size++;
			}
		}
		
		//System.out.println("//Table l1");
		//Table l1
		int l1[][]=new int[2][size];
		int initial=0;
		for(int i=0;i<10;i++)
		{
			if(((float)c1[1][i]/20)*100>=Min_Support)
			{
				l1[0][initial]=c1[0][i];
				l1[1][initial]=c1[1][i];
				//System.out.println(l1[0][initial]+" "+l1[1][initial]);
				initial++;
			}
		}
		//System.out.println("initial "+initial);
		
		int []eliminated=new int[100000];
		int e_size=0;
		int c2[][];
		
		while(size>1)
		{
			//System.out.println("//Table c2");
			//Table c2
			int c2_size=(size*(size-1))/2;
			//System.out.println("size="+size+" c2_size "+c2_size);
			c2=new int[2][c2_size];
			int z=0; // to increment value in c2
			for(int i=0;i<size;i++)
			{
				for(int j=i+1;j<size;j++)
				{
					if(l1[0][i]==l1[0][j])
						continue;
					c2[0][z]= merge(l1[0][i],l1[0][j]);
					c2[1][z]=cal_Support(d1,c2[0][z]);
					//System.out.println(z+" "+ c2[0][z]+" "+c2[1][z]);
					z++;
				}
			}
			
			//calculating size for L2
			int sizeL2=0; // size for next array -> L1 array
			for(int i=0;i<z;i++)
			{
				
				if(((float)c2[1][i]/20)*100>=Min_Support && eliminate_check(c2[0][i],eliminated,e_size)==0 )
				{
					if(eliminate_check(c2[0][i],c2[0],i)==0)
					{
						//System.out.println(c2[0][i]+" sizeL2 "+sizeL2);
						sizeL2++;
					}
				}
			}
			//System.out.println("sizeL2 "+sizeL2);
			//System.out.println("//Table lL2");
			//Table L2
			l1=new int[2][sizeL2];
			int initialL2=0;
			for(int i=0;i<z;i++)
			{
				if(((float)c2[1][i]/20)*100>=Min_Support && eliminate_check(c2[0][i],eliminated,e_size)==0 )
				{
					if(eliminate_check(c2[0][i],c2[0],i)==0)
					{
					l1[0][initialL2]=c2[0][i];
					l1[1][initialL2]=c2[1][i];
					//System.out.println(l1[0][initialL2]+" "+l1[1][initialL2]+" "+initialL2);
					associations(l1[0][initialL2],d1);
					initialL2++;
					}
				}
				else
				{
					eliminated[e_size]=c2[0][i];
					e_size++;
				}
			}
			size=sizeL2;
		}
	}
	

	public static void printdatabase(int[][] d1)
	{
		int t=101;
		System.out.print("\t");
		String[] items= {"Apple","Orange","Lemon","Ginger","Water","Milk","Chips","Diapers","iPhone","Case"};
		for(int i=0;i<10;i++)
		{
			System.out.print(items[i]+"\t");
		}
		System.out.println();
		for(int i=0;i<20;i++)
		{
			System.out.print("T"+t+"\t");
			for(int j=0;j<10;j++)
			{
				System.out.print(d1[i][j]+"\t");
			}
			System.out.println();
			t++;
		}
	}
	
	
	public static void scanfile(int d1[][],int db) throws FileNotFoundException 
	{
		
		File file = new File("/Users/amandeepverma/Desktop/database"+db+".txt");
		Scanner sc1=new Scanner(file);
		sc1.nextLine();
		System.out.println();
		
		for(int i=0;i<20;i++)
		{
			sc1.next();
			for(int j=0;j<10;j++)
			{
				//System.out.println(sc1.next());
				d1[i][j]=Integer.parseInt(sc1.next());
			}
		}
		sc1.close();
		printdatabase(d1);
	}
	
	
	public static void main(String[] args) throws FileNotFoundException   {
		Scanner sc=new Scanner(System.in);
		for(int db=1;db<6;db++)
		{
			System.out.print("Enter the Minimum Support for dataBase"+db+" :");
			Min_Support=sc.nextFloat();
			
			System.out.print("Enter the Minimum for dataBase"+db+" :");
			Min_Confidence=sc.nextFloat();
			
			int d1[][] = new int[20][10];
			scanfile(d1,db);
			
			System.out.println("\nFor DataBase"+db+" with Min Support= "+Min_Support+" MinConfidence="+Min_Confidence);
			System.out.println("+++++------ASSOCIATION RULES------+++++");
			
			Table_creation(d1);
			System.out.println("\n");
		}
		sc.close();
	}
}
