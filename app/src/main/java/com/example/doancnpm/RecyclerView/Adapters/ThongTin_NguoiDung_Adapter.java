package com.example.doancnpm.RecyclerView.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Users;
import com.example.doancnpm.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class ThongTin_NguoiDung_Adapter extends FirebaseRecyclerAdapter<Users,ThongTin_NguoiDung_Adapter.myviewHolder>{


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ThongTin_NguoiDung_Adapter(@NonNull FirebaseRecyclerOptions<Users> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ThongTin_NguoiDung_Adapter.myviewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Users model) {

        FirebaseAuth authProfile;
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();


        holder.txtThuHang.setText(model.getThuHang());
        holder.txtName.setText(firebaseUser.getDisplayName());
        holder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.txtName.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_user_admin))
                        .setExpanded(true,1200)
                        .create();

                dialogPlus.show();

                View view = dialogPlus.getHolderView();
                EditText name = view.findViewById(R.id.edtNameEdit);
                EditText thuhang = view.findViewById(R.id.edtThuHang);
                Button hoantat = view.findViewById(R.id.btnHoanTat);



                name.setText(firebaseUser.getDisplayName());
                thuhang.setText(model.getThuHang());


                hoantat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map=    new HashMap<>();

                        map.put("thuHang",thuhang.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.txtName.getContext(),"cập nhật thành công ",Toast.LENGTH_LONG).show();
                                        dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.txtName.getContext(),"cập nhật không thành công !! ",Toast.LENGTH_LONG).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });







            }
        });

        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.txtName.getContext());
                builder.setTitle("Bạn có chắc chắn xóa ?");
                builder.setMessage("Dữ liệu sẽ không thể được khôi phục");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(getRef(position).getKey()).removeValue();
                        Toast.makeText(holder.txtName.getContext(), "đã xóa ", Toast.LENGTH_SHORT).show();


                    }
                }) ;

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.txtName.getContext(), "đã hủy ", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });












    }

    @NonNull
    @Override
    public ThongTin_NguoiDung_Adapter.myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);

        return new myviewHolder(view);
    }

    class myviewHolder extends RecyclerView.ViewHolder{
        TextView txtName, txtThuHang;
        Button btnSua,btnXoa;



        public myviewHolder(@NonNull View itemView) {
            super(itemView);

            txtName =itemView.findViewById(R.id.txtTenTK);
            txtThuHang=itemView.findViewById(R.id.txtThuHang);
            btnSua =itemView.findViewById(R.id.btnSUA);
            btnXoa=itemView.findViewById(R.id.btnXOA);
        }
    }
}


