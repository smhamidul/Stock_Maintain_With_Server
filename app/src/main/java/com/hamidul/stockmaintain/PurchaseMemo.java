package com.hamidul.stockmaintain;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class PurchaseMemo extends Fragment {

    RecyclerView recyclerView;
    Button button;
    MyAdapter myAdapter;
    HashMap<String,String> hashMap;
    MyDatabase myDatabase;
    SQLiteDatabaseHelper sqLiteDatabaseHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_purchase_memo, container, false);

        recyclerView = myView.findViewById(R.id.recyclerView);
        button = myView.findViewById(R.id.button);
        myDatabase = new MyDatabase(getActivity());
        sqLiteDatabaseHelper = new SQLiteDatabaseHelper(getActivity());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int x=0; x<AddPurchase.purchaseUnit.size(); x++){
                    hashMap = AddPurchase.purchaseUnit.get(x);
                    String id = hashMap.get("id");
                    int unit = Integer.parseInt(hashMap.get("unit")) + sqLiteDatabaseHelper.getStockOldUnit(id);
                    for (HashMap item : AddPurchase.purchaseUnit){
                        if (item.get("id").equals(id)){
                            myDatabase.updateStock(id,unit);
                        }
                    }
                }
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.stock);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout,new Stock());
                fragmentTransaction.commit();

            }
        });

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return myView;
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder>{

        @NonNull
        @Override
        public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View myView = inflater.inflate(R.layout.stock_item,parent,false);
            return new myViewHolder(myView);
        }

        @Override
        public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

            hashMap = AddPurchase.purchaseUnit.get(position);
            String id = hashMap.get("id");
            String sku = hashMap.get("sku");
            String unit = hashMap.get("unit");

            holder.skuName.setText(sku);
            holder.skuUnit.setText(unit);


        }

        @Override
        public int getItemCount() {
            return AddPurchase.purchaseUnit.size();
        }

        public class myViewHolder extends RecyclerView.ViewHolder{
            TextView skuName,skuUnit;
            public myViewHolder(@NonNull View itemView) {
                super(itemView);

                skuName = itemView.findViewById(R.id.skuName);
                skuUnit = itemView.findViewById(R.id.skuUnit);

            }
        }
    }

}