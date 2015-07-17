package dop54321.com.androidfinalex;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by dop54321 on 17/07/2015.
 */
public class MakeMessage {
    public static void show(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
