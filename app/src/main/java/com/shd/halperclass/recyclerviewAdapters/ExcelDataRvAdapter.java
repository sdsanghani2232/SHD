package com.shd.halperclass.recyclerviewAdapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.shd.R;
import com.shd.viewmodes.ExcelFileData;
import java.util.List;

public class ExcelDataRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM= 0 ;
    private static final int VIEW_TYPE_EMPTY= 1 ;
    TextView totalDesign,totalErrorDesign;
    Context context;
    private static int errorCount;

    ExcelFileData data = ExcelFileData.getInstance();
    List<List<Object>> sheetData = data.getExcelDataList();

    public ExcelDataRvAdapter(TextView totalDesign, TextView totalErrorDesign) {
        this.totalDesign = totalDesign;
        this.totalErrorDesign = totalErrorDesign;
        errorCount = -1;
        setErrorCount(true);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if(viewType == VIEW_TYPE_EMPTY)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.excel_empty_data_view,parent,false);
            return new EmptyDataView(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.excel_data_view,parent,false);
            return new DataView(view);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof DataView)
        {
            List<Object> row = sheetData.get(position);
            ((DataView)holder).bindDataView(row);

            ((DataView)holder).update.setOnClickListener(v->{
//                Context context = v.getContext();
//                Intent intent = new Intent(context, UpdateActivity.class);
//                intent.putExtra("position",position);
//                context.startActivity(intent);
//                ((DataView)holder).complete();
            });

            ((DataView)holder).delete.setOnClickListener(v -> {
                sheetData.remove(position);
                notifyDataSetChanged();
                errorCount = 0;
            });

        }
    }



    @SuppressLint("SetTextI18n")
    @Override
    public int getItemCount() {
        totalDesign.setText("Total number of Design : " + sheetData.size());
        totalErrorDesign.setText("Error Designs : "+errorCount);
        return sheetData.isEmpty() ? 1 : sheetData.size();
    }


    public int getItemViewType(int position)
    {
        return sheetData.isEmpty() ? VIEW_TYPE_EMPTY : VIEW_TYPE_ITEM;
    }

    public static void setErrorCount(boolean increment) {
        if (errorCount >= 0 )
        {
            if(increment) errorCount++;
            else errorCount--;
        }else {
            if (increment) errorCount++;
        }
    }

    public static class DataView extends RecyclerView.ViewHolder {
        ShapeableImageView img1,img2,statusImg;
        MaterialTextView companyName,designCode,type,date;
        MaterialCardView cardView;
        Button delete,update;
        public DataView(@NonNull View itemView) {

            super(itemView);
            img1 = itemView.findViewById(R.id.card_img1);
            img2 = itemView.findViewById(R.id.card_img2);
            companyName = itemView.findViewById(R.id.card_company_name_text);
            designCode = itemView.findViewById(R.id.card_deign_code_text);
            type = itemView.findViewById(R.id.card_jw_type);
            date = itemView.findViewById(R.id.card_date);
            cardView = itemView.findViewById(R.id.card_view);
            statusImg = itemView.findViewById(R.id.card_status_img);
            delete = itemView.findViewById(R.id.card_delete_button);
            update = itemView.findViewById(R.id.card_update_button);
        }

        @SuppressLint("SetTextI18n")
        public void bindDataView(List<Object> row) {
            Bitmap img1Data ,img2Data;
            cardView.setStrokeColor(ContextCompat.getColor(itemView.getContext(),R.color.cv_stroke_color));
            if(!row.get(0).equals("null")) {
                img1Data = (Bitmap) row.get(0);
                img1.setImageBitmap(img1Data);
            }else img1.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.default_img));

            if(!row.get(1).equals("null")) {
                img2Data =(Bitmap)  row.get(0);
                img2.setImageBitmap(img2Data);
            }else img2.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.default_img));

            companyName.setText(row.get(2).equals("null") ? "SHD" : row.get(2).toString());
            designCode.setText(row.get(3).equals("null") ? "NO Design Code" : row.get(3).toString());
            date.setText(row.get(8).toString());
            type.setText(row.get(9) + "("+row.get(10)+")");

            if(row.get(3).equals("null") && row.get(4).equals("null") && row.get(5).equals("null")) {
                cardView.setStrokeColor(ContextCompat.getColor(itemView.getContext(),R.color.cv_stroke_error_color));
                setErrorCount(true);
            }
            else if(row.get(9).equals("null") || row.get(10).equals("null")) {
                cardView.setStrokeColor(ContextCompat.getColor(itemView.getContext(),R.color.cv_stroke_error_color));
                setErrorCount(true);
            }

            if(row.get(11).equals("true")) statusImg.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.complet_mark));
            else statusImg.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.clock));
        }

        public void complete()
        {
            if(itemView.getContext() instanceof Activity)
            {
                ((Activity) itemView.getContext()).finish();
            }
        }
    }

    public static class EmptyDataView extends RecyclerView.ViewHolder {
        public EmptyDataView(@NonNull View itemView) {
            super(itemView);
        }
    }

}
