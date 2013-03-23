package gh.polyu.utility;

public class StringOper {

	static public boolean IsAllEnglishLetterORNumberSign(String str)
	{
		str = str.toLowerCase();	
		for(int i=0;i<str.length();i++)
		{	
			if((str.charAt(i) <= 'a' || str.charAt(i) >= 'z') && str.charAt(i) != '#')
			{	
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(StringOper.IsAllEnglishLetterORNumberSign("Hello"));
		
	}
}
