package com.foi_bois.zisprojekt.firebase;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.foi_bois.zisprojekt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Auth {
    private static FirebaseAuth mAuth;
    private static FirebaseUser currUser;
    private static AuthCredential creds;

    public static void removeAcct(final Context ctx){
        currUser.reauthenticate(creds).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(currUser != null)
                    return;

                currUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            Toast.makeText(ctx, ctx.getString(R.string.uspjesno_brisanje_racuna), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(ctx, ctx.getString(R.string.neuspjeno_brisanje_racuna), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public static void logOut(Context ctx){
        mAuth.signOut();
    }
}
