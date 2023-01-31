package com.ssafy.dietre.ui.pick;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.ssafy.dietre.MainActivity;
import com.ssafy.dietre.R;
import com.ssafy.dietre.api.ApiCallService;
import com.ssafy.dietre.api.data.MealData;
import com.ssafy.dietre.api.response.DetectionRes;
import com.ssafy.dietre.api.response.MealRegisterRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.common.type.MealTime;
import com.ssafy.dietre.databinding.FragmentPickBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;


public class PickFragment extends Fragment {

    public FragmentPickBinding binding;
    public PickViewModel pickViewModel;
    public FooddialogFragment dialog;
    public LoadingFragment loading;
    public RecyclerView foodList;
    public FoodItemAdapter adapter;

    static final int REQUEST_PERMISSION = 1;
    static ArrayList<String> food_list; // 보여줄 음식리스트
    static int removeId = -1; // 삭제할 음식리스트
    static Long pick; // 식단 넣을 날짜
    static MealTime type; // 식사 종류
    public File photoDir; // 임시 저장 경로(캐시)
    public File photo; // 임시 저장한 사진
    public String date; // 사진 찍은 or 불러온 날짜
    public String photoName; // 저장할 사진 이름
    String imgUrl; // 불러온 사진 경로
    public LinearLayout layoutPic;
    public Button btnPic;
    public Button btnDate;
    public Button meal;
    public static Context context;

    public static PickFragment getInstance() {
        PickFragment pickFragment = new PickFragment();
        return pickFragment;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        context = getParentFragment().getContext();

        binding = FragmentPickBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        foodList = (RecyclerView) binding.listFood; // 음식 보여줄 listview
        pickViewModel = new ViewModelProvider(requireActivity()).get(PickViewModel.class);

        pickViewModel.setRecyclerView(foodList);
        loading = LoadingFragment.newInstance("pick");
        pickViewModel.clearFoodList();

        // 버튼 눌러서 사진 찍고 imageView로 대체해서 띄워줌
        btnPic = (Button) binding.btnPic;
        layoutPic = (LinearLayout) binding.layoutPic;
        btnPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 권한 체크 & 허용
                checkPermission();
                // 카메라, 갤러리 중 선택창 띄우기
                Intent galleryPic = new Intent(Intent.ACTION_GET_CONTENT);
                galleryPic.setType("image/*");
                Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Intent chooser = Intent.createChooser(galleryPic, "방법 선택");
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePic});
                getPic.launch(chooser); // startActivity
            }
        });

        // 날짜 선택 버튼
        btnDate = (Button) binding.btnDatepick;
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // datePicker 캘린더 모달 띄우기
                // 유효성 제한(오늘 날짜로부터 1년전부터 이번달까지만 조회 가능, 선택도 미래는 불가능
                long now = Calendar.getInstance().getTimeInMillis();
                long tenYearsAgo = now - 31557600000L;
                pick = MaterialDatePicker.todayInUtcMilliseconds();
                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder()
                        .setOpenAt(pick)
                        .setStart(tenYearsAgo)
                        .setEnd(now)
                        .setValidator(DateValidatorPointBackward.now());
                MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("먹은 날짜 선택")
                        .setSelection(pick)
                        .setCalendarConstraints(constraintsBuilder.build())
                        .setTheme(R.style.DietRe_Calendar)
                        .build();
                datePicker.show(getActivity().getSupportFragmentManager(), "날짜 선택");
                // 날짜 선택
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        pick = selection;
                        customDate(pickViewModel);
                    }
                });
            }
        });

        // 식사 종류 선택
        meal = (Button) binding.btnMeal;
        meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // menu 드롭다운 띄우기(material 3)
                PopupMenu popup = new PopupMenu(getContext(), view, Gravity.END, 0, R.style.DietRe_Popup);
                popup.getMenuInflater().inflate(R.menu.meal_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // 선택한 식사에 맞게 식사 종류 저장 & 배경색 바꿔 표기
                        switch (menuItem.getItemId()) {
                            case R.id.breakfast:
                                meal.setText(R.string.breakfast);
                                type = MealTime.breakfast;
                                pickViewModel.setType(type);
                                meal.setBackgroundColor(Color.parseColor("#ffd400"));
                                break;
                            case R.id.lunch:
                                meal.setText(R.string.lunch);
                                type = MealTime.lunch;
                                pickViewModel.setType(type);
                                meal.setBackgroundColor(Color.parseColor("#ffd400"));
                                break;
                            case R.id.dinner:
                                meal.setText(R.string.dinner);
                                type = MealTime.dinner;
                                pickViewModel.setType(type);
                                meal.setBackgroundColor(Color.parseColor("#ffd400"));
                                break;
                        }
                        return PickFragment.super.onOptionsItemSelected(menuItem);
                    }
                });
                popup.show();
            }
        });

        Button btnAdd = (Button) binding.btnAddfood;
        Button btnSave = (Button) binding.btnPicksave;

        // 음식추가
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                dialog = FooddialogFragment.getInstance(null, -1L);
                dialog.show(fm, "음식 추가 Dialog");
            }
        });

        // 식단 저장
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pickViewModel.removeFood(removeId);
                if(pickViewModel.checkAllData()) {
                    // 날짜, 식사종류, 사진, 모달 데이터 선택까지 모두 마쳤을때만 작동하도록 조건
                    MealData mealData = new MealData(pickViewModel.getPick(), pickViewModel.getType(), imgUrl, pickViewModel.getFoodIdList());
                    ApiCallService service = ApiCallServiceProvider.provide();
                    Call<MealRegisterRes> call = service.registerMealPlanner(mealData);
                    call.enqueue(new Callback<MealRegisterRes>() {
                        @Override
                        public void onResponse(Call<MealRegisterRes> call, Response<MealRegisterRes> response) {
                            if(response.isSuccessful()) {
                                pickViewModel.clearFoodList();
                                ((MainActivity)getActivity()).changeFragment(R.id.layout_pick, ResolveFragment.getInstance(pickViewModel.getPick(), pickViewModel.getType()));
                            } else {
                                Log.e("식단 등록 실패", "not Successful");
                            }
                        }

                        @Override
                        public void onFailure(Call<MealRegisterRes> call, Throwable t) {
                            Log.e("식단 등록 실패", "onFailure");
                        }
                    });

                }
            }
        });




        return root;
    }

    // 날짜 선택 버튼에 보여줄 날짜 세팅
    private void customDate(PickViewModel pickViewModel) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy.MM.dd");
        Date date = new Date();
        date.setTime(pick);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayNo = calendar.get(calendar.DAY_OF_WEEK);
        String[] day = {"일", "월", "화", "수", "목", "금", "토"};

        String strDate = dateFormat.format(date);
        pickViewModel.setPick(strDate);
        btnDate.setText(strDate+" ("+day[dayNo-1]+")");
        btnDate.setBackgroundColor(Color.parseColor("#ffd400")); // 선택하면 배경색 바꿔 표기
    }


    private void checkPermission() {
        int permissionCaemra = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        int permissionRead = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWrite = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCaemra != PackageManager.PERMISSION_GRANTED || permissionRead != PackageManager.PERMISSION_GRANTED || permissionWrite != PackageManager.PERMISSION_GRANTED) {
            {
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            }
        }
    }

    // chooser 호출
    ActivityResultLauncher<Intent> getPic = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                ImageView img = settingImg();
                Intent intent = result.getData();
                Bitmap imgBitmap = null; // 사용자가 보낼 사진(찍거나 선택한 사진)의 bitmap
                File tmpImg = null; // jpeg 형태로 캐시에 저장된 imgBitmap, 서버에 보낼 이미지
                Bitmap bitmap = null; // 서버에서 받아올 라벨링된 이미지 파일의 bitmap -> 보여줄 이미지
                if(intent.getData() != null) { // gallery
                    try {
                        Cursor cursor = getActivity().getContentResolver().query(intent.getData(), null, null, null, null);
                        int nameIdx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

                        cursor.moveToFirst();
                        photoName = cursor.getString(nameIdx);
                        date = photoName.split("_")[0];
                        photo = new File(photoDir, photoName);
                        // 선택한 파일 불러오기
                        InputStream input = getActivity().getContentResolver().openInputStream(intent.getData());
                        imgBitmap = BitmapFactory.decodeStream(input); // 저장소에 저장할 이미지
                        input.close();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                } else if(intent.getExtras() != null) { // camera
                    imgBitmap = (Bitmap) intent.getExtras().get("data"); // 찍은 사진의 bitmap
                    date = new SimpleDateFormat("yyyyMMdd").format(new Date());
                    photoName = date + "_DietRe";
                }

                // 내부 저장소 캐시에 저장
                tmpImg = new File(getActivity().getCacheDir(), photoName);
                try {
                    tmpImg.createNewFile();
                    FileOutputStream out = new FileOutputStream(tmpImg);
                    // TODO 서버에서 받아온 이미지 bitmap으로 보여줌 -> URI에서 비트맵 전환하는법으로 띄워야할듯
                    imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // 원본파일 크기로 jpeg로 저장
                    out.close(); // 사용 완료 -> tmpImg 에 imgBitmap이 JPEG 형태로 저장됨!!!

                    // Uri 타입의 파일경로를 가지는 RequestBody 객체 생성
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), tmpImg);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file", tmpImg.getName(), requestFile);

                    if(imgUrl == null) {
                        loading.show(getActivity().getSupportFragmentManager(), "loading dialog");
                    }

                    ApiCallService service = ApiCallServiceProvider.provide();
                    Call<DetectionRes> call = service.getDetectedRecords(body);
                    call.enqueue(new Callback<DetectionRes>() {
                        @Override
                        public void onResponse(Call<DetectionRes> call, Response<DetectionRes> response) {
                            if (response.isSuccessful()) {
                                List<String> list = response.body().getDetected();
                                if(list.size() == 0) {
                                    Log.e("음식 없음", String.valueOf(list.size()));
                                }
                                food_list = new ArrayList<>();
                                for(String s: list) {
                                    food_list.add(s);
                                }
                                // 인식된 메뉴 foodList RecyclerView 세팅
                                LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                foodList.setLayoutManager(manager);
                                foodList.setHasFixedSize(true);
                                // 불러온 사진 띄우기
                                if(response.body().getPath() != null) {
                                    imgUrl = response.body().getPath();
                                    try {
                                        checkpolicy();
                                        URL url = new URL(imgUrl);
                                        Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                        img.setImageBitmap(bitmap);
                                        layoutPic.addView(img);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                // 분석한 음식들 arraylist에 저장
                                pickViewModel.setFoodList(food_list);
                                LiveData<ArrayList<FoodItem>> listFoodItem = pickViewModel.getFoodList();
                                adapter = new FoodItemAdapter(getInstance(), listFoodItem.getValue(), true);
                                adapter.setXClickListener(new FoodItemAdapter.xClickListener() {
                                    @Override
                                    public void removeFood(int position) {
                                        pickViewModel.removeFood(position);
                                        listRefresh(pickViewModel);
                                    }
                                });
                                foodList.setAdapter(adapter);

                                if(imgUrl != null) {
                                    loading.getDialog().dismiss();
                                }
                            } else {
                                Log.e("사진 보내기 실패", response.toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<DetectionRes> call, Throwable t) {
                            Log.e("사진 Failure", t.getMessage());

                        }
                    });
                    tmpImg.deleteOnExit(); // call 끝나면 cache 지우기
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("이미지 없음", "X");
                }

            }
        }
            });

    // 버튼 지우고 그 위치에 imageview 만들어줌
    public ImageView settingImg() {
        layoutPic.removeView(btnPic);
        ImageView imgPic = new ImageView(getActivity());
        imgPic.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return imgPic;
    }

    private void checkpolicy() {
        if (Build.VERSION.SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }


    public void listRefresh(PickViewModel pickViewModel) {
        adapter = new FoodItemAdapter(getInstance(), pickViewModel.getFoodList().getValue(), true);
        adapter.setXClickListener(new FoodItemAdapter.xClickListener() {
            @Override
            public void removeFood(int position) {
                pickViewModel.removeFood(position);
            }
        });
        pickViewModel.getRecyclerView().setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}