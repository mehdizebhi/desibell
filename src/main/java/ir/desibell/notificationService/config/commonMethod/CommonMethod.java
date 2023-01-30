package ir.desibell.notificationService.config.commonMethod;

import java.util.Random;

public class CommonMethod {

    public static Long generatedRandomLong(int leftLimit, int rightLimit) {
        Random random = new Random();
        if (leftLimit < rightLimit) {
            return Long.valueOf(random.nextInt(rightLimit - leftLimit) + leftLimit);
        }
        return Long.valueOf(0);
    }
    
    public static String changeToPersianNumber(String str){
        String str2 = "";
        for(int i = 0; i < str.length(); i++){
            switch(str.charAt(i)){
                case '0':
                    str2 += "۰";
                    break;
                case '1':
                    str2 += "۱";
                    break;
                case '2':
                    str2 += "۲";
                    break;
                case '3':
                    str2 += "۳";
                    break;
                case '4':
                    str2 += "۴";
                    break;
                case '5':
                    str2 += "۵";
                    break;
                case '6':
                    str2 += "۶";
                    break;
                case '7':
                    str2 += "۷";
                    break;
                case '8':
                    str2 += "۸";
                    break;
                case '9':
                    str2 += "۹";
                    break;
                default:
                    str2 += str.charAt(i);
                    break;
            }
        }
        
        return str2;
    }

}
