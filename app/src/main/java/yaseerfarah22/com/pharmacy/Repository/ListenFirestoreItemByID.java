package yaseerfarah22.com.pharmacy.Repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import yaseerfarah22.com.pharmacy.Interface.LiveDataListener;
import yaseerfarah22.com.pharmacy.Model.DocumentSnapListener;

/**
 * Created by DELL on 5/5/2019.
 */

public class ListenFirestoreItemByID extends LiveData<List<DocumentSnapListener>> {

    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    String userId,fieldName;

    ListenerRegistration listenerRegistration;
    MyEvent myEvent=new MyEvent();

    public static final int addedTag=1;
    public static final int modifiedTag=2;
    public static final int removedTag=3;

    private boolean is_ListenerRemove = true;
    private final Handler handler = new Handler();
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(context,"Remove",Toast.LENGTH_SHORT).show();

            listenerRegistration.remove();
            liveDataListener.onInActive();
            is_ListenerRemove = true;
        }
    };
    private LiveDataListener liveDataListener;

    Context context;
    public ListenFirestoreItemByID(String collectionRef, String userId, String fieldName, Context context, LiveDataListener liveDataListener) {
        this.firebaseFirestore=FirebaseFirestore.getInstance();
        this.userId=userId;
        this.fieldName=fieldName;
        this.collectionReference=firebaseFirestore.collection(collectionRef);
        this.context=context;
        this.liveDataListener=liveDataListener;
        this.liveDataListener.onActive();
    }

    @Override
    protected void onActive() {

        if(is_ListenerRemove){
            this.listenerRegistration=this.collectionReference.whereEqualTo(fieldName,userId).addSnapshotListener(myEvent);


        }
        else {
            handler.removeCallbacks(removeListener);
        }

        is_ListenerRemove=false;

       // Toast.makeText(context,"active",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onInactive() {

        //Toast.makeText(context,"no",Toast.LENGTH_SHORT).show();
       // listenerRegistration.remove();
        //Toast.makeText(context,"INAACTIVE",Toast.LENGTH_SHORT).show();
       // handler.postDelayed(removeListener,500);
        removeListener();


    }


    public void  removeListener(){
       // Toast.makeText(context,"Remove",Toast.LENGTH_SHORT).show();
        listenerRegistration.remove();
        liveDataListener.onInActive();
        is_ListenerRemove = true;

    }


    private class MyEvent implements EventListener<QuerySnapshot>{


        @Override
        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

            List<DocumentSnapListener>documentSnapListeners=new ArrayList<>();
            for (DocumentChange documentChange:queryDocumentSnapshots.getDocumentChanges()){
                switch (documentChange.getType()){

                    case ADDED:
                        documentSnapListeners.add(new DocumentSnapListener(documentChange.getDocument(),addedTag));
                        break;
                    case MODIFIED:
                        documentSnapListeners.add(new DocumentSnapListener(documentChange.getDocument(),modifiedTag));
                        break;

                    case REMOVED:
                        documentSnapListeners.add(new DocumentSnapListener(documentChange.getDocument(),removedTag));
                        break;

                }

            }

           setValue(documentSnapListeners);
        }
    }

}
