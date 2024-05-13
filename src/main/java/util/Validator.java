package util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Validator
{
    public static boolean validatePassword(String password)
    {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,12}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }
}
