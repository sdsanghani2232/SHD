package com.shd.ui.fragments.subFragments.addJewellery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shd.R;
import com.shd.halperclass.otherClass.CheckInternet;
import com.shd.viewmodes.ExcelFileData;
import com.shd.viewmodes.ExcelImgData;
import com.shd.ui.activity.sub_activity.excel_file.ExcelDataListActivity;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Shape;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExcelFragment extends Fragment {

    private boolean isInternet = true, status;
    private String customerName = "", date = "", workBy = "SHD", workPlace = "SHD Office";
    TextView errorText, dialogMsg;
    ScrollView mainScrollView;
    MaterialSwitch designStatus;
    Uri excelUri;
    TextInputLayout date_layout, file_name_layout;
    TextInputEditText date_text, work_by_text, work_place_text, customer_name_text, file_name_text;
    Button selectFile, save, dialogCancel;
    View dialogBoxView;
    LottieAnimationView animationView;
    AlertDialog dialog;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private FutureTask<Boolean> futureTask;

    public ExcelFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_excel, container, false);

        findIds(view);
        checkInternet();
        currentDate();
        date_layout.setStartIconOnClickListener(v -> setDate());
        date_text.setOnClickListener(v -> setDate());

        selectFile.setOnClickListener(v -> {
            file_name_layout.setErrorEnabled(false);
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/vnd.ms-excel");
            String[] mimeTypes = {"application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            filePickerLauncher.launch(intent);
        });
        save.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    checkInternet();
                    if (isInternet) {
                        checkData();
                        save.setBackgroundColor(requireContext().getColor(R.color.form_button_background_onClick_color));
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (isInternet) {
                        save.setBackgroundColor(requireContext().getColor(R.color.form_button_background_color));
                    } else {
                        save.setBackgroundColor(requireContext().getColor(R.color.form_not_enable_button_color));
                    }
                    break;
            }
            return true;
        });
        return view;
    }


    private void findIds(View view) {
        customer_name_text = view.findViewById(R.id.excel_customer_input_text);
        designStatus = view.findViewById(R.id.excel_status_switch);
        work_by_text = view.findViewById(R.id.excel_work_by_input_text);
        work_place_text = view.findViewById(R.id.excel_work_place_input_text);
        date_text = view.findViewById(R.id.excel_date_display_text);
        date_layout = view.findViewById(R.id.excel_date_layout);
        errorText = view.findViewById(R.id.excel_error_text);
        mainScrollView = view.findViewById(R.id.excel_main_scroll_view);
        file_name_layout = view.findViewById(R.id.excel_file_name_layout);
        file_name_text = view.findViewById(R.id.excel_file_name_text);
        selectFile = view.findViewById(R.id.excel_select_file);
        save = view.findViewById(R.id.excel_save_button);
    }

    private void checkInternet() {
        if (new CheckInternet(requireContext()).Check()) {
            isInternet = true;
            save.setClickable(true);
            save.setBackgroundColor(requireContext().getColor(R.color.form_button_background_color));
            errorText.setVisibility(View.GONE);
        } else {
            isInternet = false;
            save.setClickable(false);
            save.setBackgroundColor(requireContext().getColor(R.color.form_not_enable_button_color));
            errorText.setText(requireContext().getResources().getString(R.string.check_internet_connectivity));
            errorText.setVisibility(View.VISIBLE);
            mainScrollView.scrollTo(0, 0);
        }
    }

    private void currentDate() {
        Calendar calendars = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd yyyy", Locale.getDefault());
        date_text.setText(format.format(calendars.getTime()));
    }

    private void setDate() {
        CalendarConstraints.Builder builder = new CalendarConstraints.Builder();
        builder.setValidator(DateValidatorPointBackward.now());

        MaterialDatePicker.Builder<Long> dateBuilder = MaterialDatePicker.Builder.datePicker();
        dateBuilder.setTheme(R.style.Theme_SHD_form_calender);
        dateBuilder.setCalendarConstraints(builder.build());

        long today = MaterialDatePicker.todayInUtcMilliseconds();
        dateBuilder.setSelection(today);

        MaterialDatePicker<Long> datePicker = dateBuilder.build();

        dateBuilder.setTitleText(requireContext().getResources().getString(R.string.select_date));
        dateBuilder.setSelection(System.currentTimeMillis());

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendars = Calendar.getInstance();
            calendars.setTimeInMillis(selection);
            SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd yyyy", Locale.getDefault());
            date_text.setText(format.format(calendars.getTime()));
        });

        datePicker.show(requireActivity().getSupportFragmentManager(), "Jewellery Date");
    }
    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        file_name_text.setText(uri.getLastPathSegment());
                        excelUri = uri;
                    } else {
                        file_name_layout.setErrorEnabled(true);
                        file_name_layout.setError("Reselect File");
                    }
                }
            });

    @SuppressLint("InflateParams")
    private void checkData() {

        customerName = Objects.requireNonNull(customer_name_text.getText()).toString();
        workBy = Objects.requireNonNull(work_by_text.getText()).toString();
        workPlace = Objects.requireNonNull(work_place_text.getText()).toString();
        date = Objects.requireNonNull(date_text.getText()).toString();
        status = designStatus.isChecked();

        if (workBy.isEmpty())
            workBy = requireContext().getResources().getString(R.string.work_by_text);
        if (workPlace.isEmpty())
            workPlace = requireContext().getResources().getString(R.string.work_place_text);
        if (customerName.isEmpty()) customerName = "null";
        if (excelUri == null) {
            file_name_layout.setErrorEnabled(true);
            file_name_layout.setError("Select File");
        } else {
            save.setClickable(false);
            resetValue();
            executeTack(excelUri);
        }
    }

    private void executeTack(Uri excelUri) {
        //if other tack can be work so remove that
        if (futureTask != null && !futureTask.isDone()) {
            futureTask.cancel(true);
        }

        Callable<Boolean> task = () -> {
            try {
                InputStream inputStream = requireContext().getContentResolver().openInputStream(excelUri);
                if (inputStream != null) {
                    Workbook workbook = new XSSFWorkbook(inputStream);
                    // get all img form sheet
                    Map<Integer, List<ExcelImgData>> sheetIMGS = new HashMap<>();
                    for (int currentSheet = 0; currentSheet < workbook.getNumberOfSheets(); currentSheet++) {
                        Drawing<?> drawing = workbook.getSheetAt(currentSheet).getDrawingPatriarch();
                        List<ExcelImgData> img = new ArrayList<>();
                        if (drawing != null) {
                            for (Shape shape : drawing) {
                                Picture picture = (Picture) shape;
                                ClientAnchor anchor = picture.getClientAnchor();
                                byte[] data1 = picture.getPictureData().getData();
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data1, 0, data1.length);
                                ExcelImgData data = new ExcelImgData(anchor.getRow1(), anchor.getCol1(), bitmap);
                                img.add(data);
                            }
                        }
                        int sheetNumber = currentSheet;
                        sheetIMGS.put(++sheetNumber, img);
                    }

                    // get data form sheet also add img and other data
                    int sheetCount = 0;
                    Map<Integer, List<List<Object>>> sheetData = new HashMap<>();
                    for (Sheet sheet : workbook) {
                        List<List<Object>> rowsData = new ArrayList<>();
                        sheetCount++;
                        for (Row row : sheet) {
                            int nullCell = 0;
                            short firstCol = 0;
                            short lastCol = 11;
                            List<Object> rowData = new ArrayList<>();
                            for (short currentCol = firstCol; currentCol < lastCol; currentCol++) {
                                if (currentCol < 10) {
                                    Cell cell = row.getCell(currentCol);
                                    if (cell == null || cell.getCellType() == CellType.BLANK) {
                                        nullCell++;
                                        rowData.add("null");
                                    } else {
                                        String cellValue = cell.toString();
                                        rowData.add(cellValue.trim());
                                    }
                                } else {
                                    AtomicBoolean isImg1Set = new AtomicBoolean(false);
                                    AtomicBoolean isImg2Set = new AtomicBoolean(false);

                                    int finalSheetCount = sheetCount;
                                    sheetIMGS.forEach((integer, imgData) -> {
                                        if (integer == finalSheetCount) {
                                            imgData.forEach(imgData1 -> {
                                                if (row.getRowNum() == imgData1.getRow() && imgData1.getCol() == 10) {
                                                    rowData.add(imgData1.getBitmap());
                                                    isImg1Set.set(true);
                                                }
                                                if (row.getRowNum() == imgData1.getRow() && imgData1.getCol() == 11) {
                                                    rowData.add(imgData1.getBitmap());
                                                    isImg2Set.set(true);
                                                }
                                            });
                                        }
                                    });
                                    if (!isImg1Set.get()) rowData.add("null");
                                    if (!isImg2Set.get()) rowData.add("null");
                                }
                            }
                            List<Object> designDetails = new ArrayList<>();
                            designDetails.add(0, rowData.get(10));
                            designDetails.add(1, rowData.get(11));
                            designDetails.add(2, customerName);
                            designDetails.add(3, rowData.get(0));
                            designDetails.add(4, rowData.get(1));
                            designDetails.add(5, rowData.get(2));
                            designDetails.add(6, workBy);
                            designDetails.add(7, workPlace);
                            designDetails.add(8, date);
                            designDetails.add(9, rowData.get(3));
                            designDetails.add(10, rowData.get(4));
                            designDetails.add(11, String.valueOf(status));
                            designDetails.add(12, rowData.get(7));
                            designDetails.add(13, rowData.get(8));
                            designDetails.add(14, rowData.get(9));
                            designDetails.add(15, rowData.get(5));
                            designDetails.add(16, rowData.get(6));

                            if (nullCell < 10 && !rowData.contains("Design Code")) {
                                rowsData.add(designDetails);
                            }
                        }
                        sheetData.put(sheetCount, rowsData);
                    }
                    inputStream.close();

                    List<List<Object>> excelData = new ArrayList<>();
                    sheetData.forEach((integer, lists) -> excelData.addAll(lists));

                    ExcelFileData fileData = ExcelFileData.getInstance();
                    fileData.setExcelDataList(excelData);
                    excelData.forEach(strings -> strings.forEach(s -> Log.d("data get", s.toString())));
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        };

        futureTask = new FutureTask<>(task);

        executorService.execute(futureTask);
        showMsgDialog();
        Intent intent = new Intent(requireContext(), ExcelDataListActivity.class);
        Handler handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message message) {
                boolean result = (boolean) message.obj;
                dismissDialog();
                if (result) {
                    requireContext().startActivity(intent);
                } else {
                    showErrorDialog();
                }
            }
        };

        new Thread(()->{
            try {
                Message message = handler.obtainMessage(0,futureTask.get());
                handler.sendMessage(message);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }).start();
    }

    private void resetValue() {
        customer_name_text.setText("");
        designStatus.setChecked(false);
        work_by_text.setText(requireContext().getResources().getString(R.string.work_by_text));
        work_place_text.setText(requireContext().getResources().getString(R.string.work_place_text));
        currentDate();
        file_name_text.setText("");
        save.setClickable(true);
    }

    @SuppressLint("InflateParams")
    private void showMsgDialog() {
        LayoutInflater inflater2 = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogBoxView = inflater2.inflate(R.layout.custom_dialog_box, null);
        dialogCancel = dialogBoxView.findViewById(R.id.cancel_button);
        animationView = dialogBoxView.findViewById(R.id.lottie_animation_view);
        dialogMsg = dialogBoxView.findViewById(R.id.description);
        dialogCancel.setEnabled(false);
        dialogCancel.setVisibility(View.GONE);
        dialogMsg.setText(getResources().getString(R.string.do_not_cancel));

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireContext(), R.style.Theme_SHD_dialogBox)
                .setTitle(getResources().getString(R.string.processing))
                .setCancelable(false);

        dialogBuilder.setView(dialogBoxView);
        animationView.setAnimation("spinner_circle.json");
        dialog = dialogBuilder.create();
        animationView.playAnimation();
        dialog.show();
        Log.d("Design Code", "Dialog start");
    }
    @SuppressLint("InflateParams")
    private void showErrorDialog() {
        LayoutInflater inflater2 = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogBoxView = inflater2.inflate(R.layout.custom_dialog_box, null);
        dialogCancel = dialogBoxView.findViewById(R.id.cancel_button);
        animationView = dialogBoxView.findViewById(R.id.lottie_animation_view);
        dialogMsg = dialogBoxView.findViewById(R.id.description);
        dialogMsg.setText(getResources().getString(R.string.excel_file_read_error));
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireContext(), R.style.Theme_SHD_dialogBox)
                .setTitle(getResources().getString(R.string.processing));
        dialogBuilder.setView(dialogBoxView);
        animationView.setAnimation("warning.json");
        dialog = dialogBuilder.create();
        animationView.playAnimation();
        dialog.show();
        dialogCancel.setOnClickListener(v -> dialog.dismiss());
    }
    private void dismissDialog() {
        dialog.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        executorService.shutdown();
    }
}