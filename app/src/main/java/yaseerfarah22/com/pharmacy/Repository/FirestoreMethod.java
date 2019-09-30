package yaseerfarah22.com.pharmacy.Repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import yaseerfarah22.com.pharmacy.Interface.MyCallback;
import yaseerfarah22.com.pharmacy.Interface.MyListener;
import yaseerfarah22.com.pharmacy.Interface.MySearchListener;
import yaseerfarah22.com.pharmacy.Interface.OrderListener;
import yaseerfarah22.com.pharmacy.Model.Cart_info;
import yaseerfarah22.com.pharmacy.Model.Order_info;
import yaseerfarah22.com.pharmacy.Model.Product_info;
import yaseerfarah22.com.pharmacy.Model.User_info;
import yaseerfarah22.com.pharmacy.View.Cart;

/**
 * Created by DELL on 5/5/2019.
 */

public class FirestoreMethod {


    private FirebaseFirestore firebaseFirestore;
    public final static Query.Direction descending= Query.Direction.DESCENDING;
    public final static Query.Direction ascending= Query.Direction.ASCENDING;



    public FirestoreMethod() {

        this.firebaseFirestore=FirebaseFirestore.getInstance();

    }


    public void search(String collectionRef, String columnName, String attributeName1, String attributeName2, final Class className, final MySearchListener mySearchListener){


        CollectionReference collectionReference=firebaseFirestore.collection(collectionRef);
        final List list=new ArrayList();

        collectionReference.whereGreaterThanOrEqualTo(columnName,attributeName1).whereLessThan(columnName,attributeName2).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){

                    list.add(documentSnapshot.toObject(className));

                }
                mySearchListener.onSuccess(list);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mySearchListener.onFailure(e.getMessage().toString());
                return ;
            }
        });

    }




    public void getInfoEqualTo(String collectionRef, String columnName, String attributeName, final Class className, final MyCallback myCallback){


        CollectionReference collectionReference=firebaseFirestore.collection(collectionRef);
        final List list=new ArrayList();

        collectionReference.whereEqualTo(columnName,attributeName).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){

                    list.add(documentSnapshot.toObject(className));

                }
                myCallback.onCallback(list,"Get",null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFailure(e.getMessage().toString());
                return ;
            }
        });

    }

////////


    public void getInfoByID(String collectionRef, String id, final Class className, final MyCallback myCallback){


        CollectionReference collectionReference=firebaseFirestore.collection(collectionRef);
        final List list=new ArrayList();

        collectionReference.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    list.add(documentSnapshot.toObject(className));
                    myCallback.onCallback(list, "Get", null);

                }else {
                    myCallback.onCallback(null,null,null);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFailure(e.getMessage().toString());
                return ;
            }
        });



    }


///////

    public void getInfo(String collectionRef,final Class className, final MyCallback myCallback){
        CollectionReference collectionReference=firebaseFirestore.collection(collectionRef);
        final List list=new ArrayList();

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){

                    list.add(documentSnapshot.toObject(className));

                }
                myCallback.onCallback(list,"Get",null);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFailure(e.getMessage().toString());
                return ;
            }
        });

    }


///////

    public void getInfo(String collectionRef,final Class className,int limit,DocumentSnapshot lastResult, final MyCallback myCallback){
        CollectionReference collectionReference=firebaseFirestore.collection(collectionRef);
        final List list=new ArrayList();

        Query query;
        if (lastResult==null){
            query=collectionReference.limit(limit);

        }else {
            query=collectionReference.startAt(lastResult).limit(limit);
        }

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){

                    list.add(documentSnapshot.toObject(className));

                }

                if(queryDocumentSnapshots.size()>0){
                    myCallback.onCallback(list,"Get",queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size()-1));
                }else {
                    myCallback.onCallback(list,"Get",null);

                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFailure(e.getMessage().toString());
                return ;
            }
        });

    }




    ///////


    public void getInfoOrderBy(String collectionRef, String columnName, Query.Direction direction, final Class className, final MyCallback myCallback){

        CollectionReference collectionReference=firebaseFirestore.collection(collectionRef);
        final List list=new ArrayList();

        collectionReference.orderBy(columnName,direction).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){

                    list.add(documentSnapshot.toObject(className));

                }
                myCallback.onCallback(list,"Get",null);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFailure(e.getMessage().toString());
                return ;
            }
        });
    }




//////


    public void getInfoOrderBy(String collectionRef, String columnName, Query.Direction direction, final Class className, int limit, DocumentSnapshot lastResult, final MyCallback myCallback){

        CollectionReference collectionReference=firebaseFirestore.collection(collectionRef);
        final List list=new ArrayList();

        Query query;
        if (lastResult==null){
            query=collectionReference.orderBy(columnName,direction).limit(limit);

        }else {
            query=collectionReference.startAt(lastResult).orderBy(columnName,direction).limit(limit);
        }



        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){

                    list.add(documentSnapshot.toObject(className));

                }
                if(queryDocumentSnapshots.size()>0){
                    myCallback.onCallback(list,"Get",queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size()-1));
                }else {
                    myCallback.onCallback(list,"Get",null);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFailure(e.getMessage().toString());
                return ;
            }
        });
    }









    public String getRandomID(String collectionRef){

        CollectionReference collectionReference=firebaseFirestore.collection(collectionRef);
        return collectionReference.document().getId().toString();
    }


    public void addToDatabase(String collectionRef, Object object, String id, final MyListener myListener){
        CollectionReference collectionReference=firebaseFirestore.collection(collectionRef);

        collectionReference.document(id).set(object).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myListener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myListener.onFailure(e.getMessage().toString());
            }
        });

    }









    public void addToDatabase(String collectionRef, Object object,final MyListener myListener){
        CollectionReference collectionReference=firebaseFirestore.collection(collectionRef);

        collectionReference.add(object).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                myListener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myListener.onFailure(e.getMessage().toString());
            }
        });

    }










    public void addOrder(final List<Cart_info> cartInfoList, final String order_st, final String order_md, final String order_dt, final User_info userInfo, final OrderListener myListener){

        final CollectionReference product_R=firebaseFirestore.collection("Products");
         final CollectionReference order_R=firebaseFirestore.collection("Order");
        final CollectionReference cart_R=firebaseFirestore.collection("Cart");

        final List<Cart_info> deletedCart=new ArrayList<>();

        final int[] transactionCounter=new int[1];

        for (int i=0;i<cartInfoList.size();i++){
            final Cart_info cart_info=cartInfoList.get(i);


            firebaseFirestore.runTransaction(new Transaction.Function<Cart_info>() {
                @Nullable
                @Override
                public Cart_info apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {




                    DocumentReference productDocumentReference=product_R.document(cart_info.getProduct_id());
                    DocumentReference cartDocumentReference=cart_R.document(cart_info.getCartId());
                    DocumentSnapshot productDocumentSnapshot=transaction.get(productDocumentReference);
                    DocumentSnapshot cartDocumentSnapshot=transaction.get(cartDocumentReference);
                    Product_info product_info=productDocumentSnapshot.toObject(Product_info.class);
                    Cart_info cartInfo=cartDocumentSnapshot.toObject(Cart_info.class);

                    if (Integer.valueOf(cartInfo.getQuantity())<=Integer.valueOf(product_info.getStock())) {
                       // deletedCart.add(cartInfoList.get(i));
                        int pur = Integer.valueOf(product_info.getPurchases()) + Integer.valueOf(cartInfo.getQuantity());
                        int stoke = Integer.valueOf(product_info.getStock()) - Integer.valueOf(cartInfo.getQuantity());
                        transaction.update(productDocumentReference, "purchases", pur);
                        transaction.update(productDocumentReference, "stock", stoke);
                        transaction.delete(cartDocumentReference);
                        Order_info order_info = new Order_info(order_R.document().getId(),userInfo.getUser_id() , order_st, order_md, cart_info, order_dt, userInfo);
                        transaction.set(order_R.document(order_info.getOrder_id()), order_info);
                        return cart_info;

                    }else {
                        transaction.update(cartDocumentReference, "isOutOfQuantity", true);
                    }




                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Cart_info>() {
                @Override
                public void onSuccess(Cart_info cartInfo) {
                    if (cart_info!=null){
                        deletedCart.add(cart_info);
                    }
                    transactionCounter[0]++;
                    if (transactionCounter[0]==cartInfoList.size()){
                        myListener.onSuccess(deletedCart);
                    }

                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    myListener.onFailure(e.getMessage().toString());
                }
            });


        }









    }





    public void deleteOrder(final Order_info order_info, final MyListener myListener){

        final CollectionReference product_R=firebaseFirestore.collection("Products");
        final CollectionReference order_R=firebaseFirestore.collection("Order");


        firebaseFirestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                DocumentReference documentReference=product_R.document(order_info.getCart_info().getProduct_id());
                DocumentSnapshot documentSnapshot=transaction.get(documentReference);
                Product_info product_info=documentSnapshot.toObject(Product_info.class);
                int pur=Integer.valueOf(product_info.getPurchases())-1;
                int stoke=Integer.valueOf(product_info.getStock())+1;
                transaction.update(documentReference,"purchases",pur);
                transaction.update(documentReference,"stock",stoke);
                transaction.delete( order_R.document(order_info.getOrder_id()));

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myListener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myListener.onFailure(e.getMessage().toString());
            }
        });



    }





    public void deleteFromDatabase(String collectionRef, String id, final MyListener myListener){
        CollectionReference collectionReference=firebaseFirestore.collection(collectionRef);

        collectionReference.document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myListener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myListener.onFailure(e.getMessage().toString());
            }
        });

    }




    public void delete_AllCarts(List<Cart_info> cartInfoList, final MyListener myListener){

        CollectionReference cart_R=firebaseFirestore.collection("Cart");

        WriteBatch batch=firebaseFirestore.batch();
        for (int i=0;i<cartInfoList.size();i++){
            DocumentReference documentReference=cart_R.document(cartInfoList.get(i).getCartId());
            batch.delete(documentReference);
        }

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               myListener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myListener.onFailure(e.getMessage().toString());
            }
        });




    }


}
