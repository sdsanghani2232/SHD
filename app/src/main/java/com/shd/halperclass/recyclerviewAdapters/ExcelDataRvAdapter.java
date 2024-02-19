package com.shd.halperclass.recyclerviewAdapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.shd.R;
import com.shd.halperclass.informationclass.Codes;
import com.shd.repository.storedata.ExcelDesignStoreHelper;
import com.shd.ui.activity.sub_activity.excel_file.UpdateJWDataActivity;
import com.shd.viewmodes.ExcelFileData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExcelDataRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM= 0 ;
    private static final int VIEW_TYPE_EMPTY= 1 ;
    private final Codes codes = Codes.getInstance();
    TextView totalDesign,totalErrorDesign;
    Context context;
    private static int errorCount;
    private boolean emptyView =false;
    ExcelFileData data = ExcelFileData.getInstance();
    List<List<Object>> sheetData = data.getExcelDataList();

    List<String> mainJw = new ArrayList<>();

    public ExcelDataRvAdapter(TextView totalDesign, TextView totalErrorDesign,Context context) {
        this.totalDesign = totalDesign;
        this.totalErrorDesign = totalErrorDesign;
        this.context = context;
        errorCount = -1;
        setErrorCount(true);
        mainJw.addAll(Arrays.asList(context.getResources().getStringArray(R.array.jewellery_main_type)));
        mainJw.addAll(mainJw.size(),Arrays.asList(context.getResources().getStringArray(R.array.jewellery_ring_sub_type)));
        mainJw.addAll(mainJw.size(),Arrays.asList(context.getResources().getStringArray(R.array.jewellery_nkc_sub_type)));
        mainJw.addAll(mainJw.size(),Arrays.asList(context.getResources().getStringArray(R.array.jewellery_er_sub_type)));
        mainJw.addAll(mainJw.size(),Arrays.asList(context.getResources().getStringArray(R.array.jewellery_br_sub_type)));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_EMPTY)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.excel_empty_data_view,parent,false);
            emptyView = true;
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
            ((DataView)holder).bindDataView(row,codes,mainJw);

            ((DataView)holder).update.setOnClickListener(v->{
                Context context = v.getContext();
                Intent intent = new Intent(context, UpdateJWDataActivity.class);
                intent.putExtra("position",position);
                context.startActivity(intent);
                complete();
            });

            ((DataView)holder).delete.setOnClickListener(v -> {
                sheetData.remove(position);
                notifyDataSetChanged();
                errorCount = 0;
            });

        }
    }

    private void complete() {
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public int getItemCount() {
        totalDesign.setText("Total number of Design : " + sheetData.size());
        if(errorCount <= 0 )
        {
            totalErrorDesign.setVisibility(View.GONE);
        }else {
            totalErrorDesign.setVisibility(View.VISIBLE);
            totalErrorDesign.setText("Error Designs : "+errorCount);
        }
        return sheetData.isEmpty() ? 1 : sheetData.size();
    }


    public int getItemViewType(int position)
    {
        return sheetData.isEmpty() ? VIEW_TYPE_EMPTY : VIEW_TYPE_ITEM;
    }

    public static void setErrorCount(boolean increment) {

        if (errorCount >= 0)
        {
            if(increment) errorCount++;
            else errorCount--;
        }else {
            if (increment) errorCount++;
        }
    }

    public void storeData()
    {
        if(!emptyView)
        {
            if(errorCount <= 0)
            {
                ExcelDesignStoreHelper helper = new ExcelDesignStoreHelper(context);
                helper.store();
                complete();
            }else{
                Toast.makeText(context, "Design Have Errors..", Toast.LENGTH_SHORT).show();
            }
        }else Toast.makeText(context, "No File to Store..", Toast.LENGTH_SHORT).show();
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
        public void bindDataView(List<Object> row, Codes codes, List<String> mainJw) {
            boolean setError = false;
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

            if(row.get(3).equals("null"))
            {
                designCode.setText("NO Design Code");
                if(row.get(5).equals("null")) {
                    cardView.setStrokeColor(ContextCompat.getColor(itemView.getContext(), R.color.cv_stroke_error_color));
                    setErrorCount(true);
                    setError = true;
                }else {
                    if (row.get(5).toString().length()<6){
                        cardView.setStrokeColor(ContextCompat.getColor(itemView.getContext(), R.color.cv_stroke_error_color));
                        setErrorCount(true);
                        setError = true;
                    }else {
                        if(codes.isTempCodeExits(row.get(5).toString())) {
                            cardView.setStrokeColor(ContextCompat.getColor(itemView.getContext(), R.color.cv_stroke_error_color));
                            setErrorCount(true);
                            setError = true;
                        }
                    }
                }
            }else {
                if(row.get(3).toString().length() <6) {
                    cardView.setStrokeColor(ContextCompat.getColor(itemView.getContext(), R.color.cv_stroke_error_color));
                    setErrorCount(true);
                    setError = true;
                }else {
                    if(codes.isDesignCodeExists(row.get(3).toString())) {
                        cardView.setStrokeColor(ContextCompat.getColor(itemView.getContext(), R.color.cv_stroke_error_color));
                        setErrorCount(true);
                        setError = true;
                    }
                }
                designCode.setText(row.get(3).toString());
            }
            date.setText(row.get(8).toString());

            if(row.get(10).equals("null")) // no sub type
            {
                if(mainJw.contains(row.get(9).toString())) // main type check
                {
                    if(mainJw.indexOf(row.get(9).toString()) < 4) // main type have sub type?
                    {
                        cardView.setStrokeColor(ContextCompat.getColor(itemView.getContext(), R.color.cv_stroke_error_color));
                        if(!setError) setErrorCount(true);
                        type.setText(row.get(9).toString());
                    }else type.setText(row.get(9).toString());
                }else {
                    type.setText("No jewellery Type Set..");
                    cardView.setStrokeColor(ContextCompat.getColor(itemView.getContext(), R.color.cv_stroke_error_color));
                    if(!setError) setErrorCount(true);
                }
            }else {
                if (mainJw.contains(row.get(10).toString()))
                {
                    type.setText(row.get(9) + "( "+row.get(10)+" )");
                }else {
                    type.setText("No jewellery Type Set..");
                    cardView.setStrokeColor(ContextCompat.getColor(itemView.getContext(), R.color.cv_stroke_error_color));
                    if(!setError) setErrorCount(true);
                }
            }

            if((!row.get(12).equals("null") && Float.parseFloat((String) row.get(12)) > 100) ||
                    (!row.get(13).equals("null") && Float.parseFloat((String) row.get(13)) > 100)||
                    (!row.get(14).equals("null") && Float.parseFloat((String) row.get(14)) > 100)||
                    (!row.get(15).equals("null") && Float.parseFloat((String) row.get(15)) > 100)||
                    (!row.get(16).equals("null") && Float.parseFloat((String) row.get(16)) > 100))
            {
                cardView.setStrokeColor(ContextCompat.getColor(itemView.getContext(), R.color.cv_stroke_error_color));
                if(!setError) setErrorCount(true);
            }

            if(row.get(11).equals("true")) statusImg.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.complet_mark));
            else statusImg.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.clock));
        }
    }

    public static class EmptyDataView extends RecyclerView.ViewHolder {
        public EmptyDataView(@NonNull View itemView) {
            super(itemView);
        }
    }

}
