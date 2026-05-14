import java.io.*;
import java.util.*;

public class cd{
    
    public static void main(String[] args) throws IOException 
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) 
        {
            String input = reader.readLine();
            if (input.equals("0 0"))
                break;
            int buffer = input.indexOf(' ');
            int jack = Integer.parseInt(input.substring(0, buffer));
            int jill = Integer.parseInt(input.substring(buffer + 1));


            HashSet<String> cd = new HashSet<>(jack);
            for(int i = 0; i < jack; i++)
            {
                cd.add(reader.readLine());
            }
            for(int i = 0; i < jill; i++)
            {
                cd.add(reader.readLine());
            }
            System.out.println((jack + jill) - cd.size());
        }
        reader.close();
    }
}
