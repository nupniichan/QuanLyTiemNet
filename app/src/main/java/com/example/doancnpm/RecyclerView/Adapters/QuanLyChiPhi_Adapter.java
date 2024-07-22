package com.example.doancnpm.RecyclerView.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.ChiPhi;
import com.example.doancnpm.QuanLy.CacChucNangKhac.QuanLyChiPhiFragment;
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

public class QuanLyChiPhi_Adapter extends FirebaseRecyclerAdapter <ChiPhi,QuanLyChiPhi_Adapter.myViewHolder>{
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    QuanLyChiPhiFragment fragment = new QuanLyChiPhiFragment();
    ChiPhi chiPhi;
    public QuanLyChiPhi_Adapter(@NonNull FirebaseRecyclerOptions<ChiPhi> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull ChiPhi model) {
    holder.txtTenChiPhi.setText(model.getTenChiPhi());
    holder.txtSoTien.setText(String.valueOf(model.getSoTien()));
    holder.txtThoiGian.setText(model.getThoiGian());
    holder.txtGhiChu.setText(model.getGhiChu());
    holder.txtMaNV.setText(model.getMaNV());

        FirebaseAuth authProfile;
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        holder.btnXoaChiPhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.txtTenChiPhi.getContext());
                builder.setTitle("Bạn có chắc chắn xóa ?");
                builder.setMessage("Dữ liệu sẽ không thể được khôi phục");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        FirebaseDatabase.getInstance().getReference().child("ChiPhi")
                                .child(getRef(position).getKey()).removeValue();
                        Toast.makeText(holder.txtTenChiPhi.getContext(), "đã xóa ", Toast.LENGTH_SHORT).show();


                    }
                }) ;

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.txtTenChiPhi.getContext(), "đã hủy ", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });


        holder.btnSuaChiPhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.txtTenChiPhi.getContext())
                        .setContentHolder(new ViewHolder(R.layout.add_chiphi))
                        .setExpanded(true,1500)
                        .create();

                dialogPlus.show();

                View view = dialogPlus.getHolderView();
                EditText maCP = view.findViewById(R.id.edtChiPhiMa);
                EditText tenCP = view.findViewById(R.id.edtChiPhiTen);
                EditText ThoiGian = view.findViewById(R.id.edtChiPhiThoiGian);
                EditText maNV = view.findViewById(R.id.edtMaNVChiPhi);
                EditText SoTien = view.findViewById(R.id.edtChiPhiSoTien);
                EditText ghichu = view.findViewById(R.id.edtChiPhiGhiChu);


                Button hoantat = view.findViewById(R.id.btnTaoMoiChiPhi);
                hoantat.setText("Hoàn tất");



                if (model != null) {
                    maCP.setText(model.getMaChiPhi());
                    tenCP.setText(model.getTenChiPhi());
                    ThoiGian.setText(model.getThoiGian());
                    maNV.setText(model.getMaNV());
                    SoTien.setText(String.valueOf(model.getSoTien()));
                    ghichu.setText(model.getGhiChu());
                } else {
                    Log.e("QuanLyChiPhi_Adapter", "model is null");
                }



                hoantat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map=    new HashMap<>();

                        map.put("GhiChu",ghichu.getText().toString());
                        map.put("MaChiPhi",maCP.getText().toString());
                        map.put("MaNV",maNV.getText().toString());
                        map.put("TenChiPhi",tenCP.getText().toString());
                        map.put("ThoiGian",ThoiGian.getText().toString());
                        map.put("SoTien",Integer.parseInt(SoTien.getText().toString()));





                        FirebaseDatabase.getInstance().getReference().child("ChiPhi")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.txtTenChiPhi.getContext(),"cập nhật thành công ",Toast.LENGTH_LONG).show();
                                        dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.txtTenChiPhi.getContext(),"cập nhật không thành công !! ",Toast.LENGTH_LONG).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });
    }




    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chiphi_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenChiPhi,txtSoTien,txtThoiGian,txtGhiChu,txtMaNV;
        Button btnXoaChiPhi, btnSuaChiPhi;

        public myViewHolder(@NonNull View itemView) {

            super(itemView);


            txtTenChiPhi=itemView.findViewById(R.id.txtTenChiPhi);
            txtSoTien=itemView.findViewById(R.id.txtTien);
            txtThoiGian=itemView.findViewById(R.id.txtThoiGian);
            txtGhiChu=itemView.findViewById(R.id.txtGhiChu);
            btnXoaChiPhi = itemView.findViewById(R.id.btnXoaChiPhi);
            btnSuaChiPhi=itemView.findViewById(R.id.btnSuaChiPhi);
            txtMaNV=itemView.findViewById(R.id.txtMaNV);
        }
    }
}
