package com.kandktech.ezivizi.fragments;


import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esewa.android.sdk.payment.ESewaConfiguration;
import com.esewa.android.sdk.payment.ESewaPayment;
import com.esewa.android.sdk.payment.ESewaPaymentActivity;
import com.kandktech.ezivizi.DbHandler;
import com.kandktech.ezivizi.MainActivity;
import com.kandktech.ezivizi.QRGenerateActivity;
import com.kandktech.ezivizi.R;
import com.kandktech.ezivizi.authentication.CorporateQRGenerate;
import com.kandktech.ezivizi.authentication.SharedPreferenceClass;
import com.kandktech.ezivizi.corporate.CorporateActivity;
import com.kandktech.ezivizi.corporate.CorporateList;
import com.kandktech.ezivizi.corporate.IndividualActivity;
import com.kandktech.ezivizi.model_class.SavedUserDetailModelClass;
import com.kandktech.ezivizi.model_class.ServicesModelClass;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class ListFragment extends Fragment {

    View view;
    RecyclerView listView;
    FloatingTextButton fab;
    DbHandler dbHandler;

    SharedPreferenceClass sharedPreferenceClass;

    Dialog dialog;
    Button btnCor,btnInd;
    RelativeLayout corporate,individual;


    UserAdapterClass adapterClass;
    Cursor cursor = null;

    List<SavedUserDetailModelClass> userDetailModelClasses;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_list, container, false);

        sharedPreferenceClass = new SharedPreferenceClass(getActivity());

        fab = view.findViewById(R.id.action_button);


        listView = view.findViewById(R.id.recyclerView);

        dbHandler = new DbHandler(getActivity());

        userDetailModelClasses = new ArrayList<>();

        cursor = dbHandler.viewData();

//        corporate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), CorporateList.class));
//            }
//        });
//
//        individual.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), IndividualActivity.class));
//            }
//        });


        if (cursor != null){

            if (cursor.moveToFirst()) {
                do {
                    SavedUserDetailModelClass userDetail = new SavedUserDetailModelClass();

                    userDetail.setWeb(cursor.getString(cursor.getColumnIndex(DbHandler.user_website)));
                    userDetail.setUser_logo(cursor.getString(cursor.getColumnIndex(DbHandler.user_logo)));
                    userDetail.setUsed_layout(cursor.getString(cursor.getColumnIndex(DbHandler.used_layout)));
                    userDetail.setPosition(cursor.getString(cursor.getColumnIndex(DbHandler.user_position)));
                    userDetail.setPhone(cursor.getString(cursor.getColumnIndex(DbHandler.user_phone)));
                    userDetail.setName(cursor.getString(cursor.getColumnIndex(DbHandler.user_name)));
                    userDetail.setEmail(cursor.getString(cursor.getColumnIndex(DbHandler.user_email)));
                    userDetail.setDevice_id(cursor.getString(cursor.getColumnIndex(DbHandler.user_device_id)));
                    userDetail.setColorCode(cursor.getString(cursor.getColumnIndex(DbHandler.color_code)));
                    userDetail.setAddress(cursor.getString(cursor.getColumnIndex(DbHandler.user_address)));
                    userDetail.setCompany(cursor.getString(cursor.getColumnIndex(DbHandler.company)));
                    userDetail.setFaxNo(cursor.getString(cursor.getColumnIndex(DbHandler.fax_no)));
                    userDetail.setPoBoxNo(cursor.getString(cursor.getColumnIndex(DbHandler.po_box_no)));

//                    if (cursor.getString(cursor.getColumnIndex(DbHandler.user_name)).equals("corporate")){

                    userDetailModelClasses.add(userDetail);

//                    }

                } while (cursor.moveToNext());

            }

            adapterClass = new UserAdapterClass(getActivity(),userDetailModelClasses);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            listView.setLayoutManager(mLayoutManager);
            listView.setItemAnimator(new DefaultItemAnimator());
            listView.setHasFixedSize(true);
            listView.setAdapter(adapterClass);
            cursor.requery();
            adapterClass.notifyDataSetChanged();



        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), CorporateQRGenerate.class));

//                dialog = new Dialog(getActivity(), R.style.Dialog);
//                dialog.setContentView(R.layout.qrgenereate);
//                dialog.setTitle("Choose Card Type!!!");
//
//                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//
//                dialog.setCanceledOnTouchOutside(true);
//
//                btnCor = dialog.findViewById(R.id.btnCorporate);
//                btnInd = dialog.findViewById(R.id.btnIndividual);
//
//                btnCor.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent(getActivity(), CorporateQRGenerate.class));
//                        dialog.dismiss();
//                    }
//                });
//
//                btnInd.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        startActivity(new Intent(getActivity(), QRGenerateActivity.class));
//                        dialog.dismiss();
//                    }
//                });
//
//                dialog.show();


            }
        });

        setHasOptionsMenu(true);




        return view;
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_main, menu);
        SearchManager SManager =  (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchViewAction = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        if (SManager != null) {
            searchViewAction.setSearchableInfo(SManager.getSearchableInfo(getActivity().getComponentName()));
        }
        searchViewAction.setIconifiedByDefault(true);

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {
//                adapterClass.getFilter().filter(newText);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
//                adapterClass.getFilter().filter(query);
                System.out.println("on query submit: "+query);
                return true;
            }
        };
        searchViewAction.setOnQueryTextListener(textChangeListener);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search:

                return true;

//            case R.id.edit:
//                if (sharedPreferenceClass.getName().equals("corporate")){
//
//                    startActivity(new Intent(getActivity(),CorporateQRGenerate.class));
//
//                }else{
//
//                    startActivity(new Intent(getActivity(),QRGenerateActivity.class));
//
//
//                }
//
//
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class UserAdapterClass extends RecyclerView.Adapter<UserAdapterClass.MyViewHolder> implements Filterable {

        Context context;
        List<SavedUserDetailModelClass> savedUserDetailModelClassList;
        List<SavedUserDetailModelClass> filtersavedUserDetailModelClassList;
        List<ServicesModelClass> servicesModelClassList;

        public UserAdapterClass(Context context, List<SavedUserDetailModelClass> savedUserDetailModelClassList) {
            this.context = context;
            this.savedUserDetailModelClassList = savedUserDetailModelClassList;
            filtersavedUserDetailModelClassList = new ArrayList<>(savedUserDetailModelClassList);
        }


        @NonNull
        @Override
        public UserAdapterClass.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.corporate_list_item,viewGroup,false);

            return new UserAdapterClass.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final UserAdapterClass.MyViewHolder holder, int i) {
            servicesModelClassList = new ArrayList<>();
            SavedUserDetailModelClass savedUserDetailModelClass = savedUserDetailModelClassList.get(i);

            try {
                Cursor cursor = dbHandler.viewServices(savedUserDetailModelClass.getDevice_id());
                if (cursor != null){
                    if (cursor.moveToFirst()) {
                        do {

                            ServicesModelClass servicesModelClass1 = new ServicesModelClass();

                            servicesModelClass1.setService6(cursor.getString(cursor.getColumnIndex(DbHandler.service_six)));
                            servicesModelClass1.setService5(cursor.getString(cursor.getColumnIndex(DbHandler.service_five)));
                            servicesModelClass1.setService4(cursor.getString(cursor.getColumnIndex(DbHandler.service_four)));
                            servicesModelClass1.setService3(cursor.getString(cursor.getColumnIndex(DbHandler.service_three)));
                            servicesModelClass1.setService2(cursor.getString(cursor.getColumnIndex(DbHandler.service_two)));
                            servicesModelClass1.setService1(cursor.getString(cursor.getColumnIndex(DbHandler.service_one)));

                            servicesModelClassList.add(servicesModelClass1);

                        } while (cursor.moveToNext());

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

//            ServiceAdapterClass adapter = new ServiceAdapterClass(servicesModelClassList);
//            holder.list3.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//            holder.list3.setHasFixedSize(true);
//            holder.list3.setAdapter(adapter);

            ServiceAdapterClass adapter = new ServiceAdapterClass(servicesModelClassList);
            holder.listView.setLayoutManager(new LinearLayoutManager(getActivity()));
            holder.listView.setHasFixedSize(true);
            holder.listView.setAdapter(adapter);

            holder.listView1.setLayoutManager(new LinearLayoutManager(getActivity()));
            holder.listView1.setHasFixedSize(true);
            holder.listView1.setAdapter(adapter);

            holder.listView2.setLayoutManager(new LinearLayoutManager(getActivity()));
            holder.listView2.setHasFixedSize(true);
            holder.listView2.setAdapter(adapter);

            holder.listView3.setLayoutManager(new LinearLayoutManager(getActivity()));
            holder.listView3.setHasFixedSize(true);
            holder.listView3.setAdapter(adapter);

            holder.listView4.setLayoutManager(new LinearLayoutManager(getActivity()));
            holder.listView4.setHasFixedSize(true);
            holder.listView4.setAdapter(adapter);

            holder.listView5.setLayoutManager(new LinearLayoutManager(getActivity()));
            holder.listView5.setHasFixedSize(true);
            holder.listView5.setAdapter(adapter);

            System.out.println("UsedLayout : "+savedUserDetailModelClass.getUsed_layout());

            if (savedUserDetailModelClass.getUsed_layout().equals("1")){
                holder.easyFlipView.setVisibility(View.GONE);
                holder.easyFlipView2.setVisibility(View.GONE);
                holder.easyFlipView3.setVisibility(View.GONE);
                holder.easyFlipView4.setVisibility(View.GONE);
                holder.easyFlipView1.setVisibility(View.GONE);
                holder.easyFlipView5.setVisibility(View.VISIBLE);

            }
            if (savedUserDetailModelClass.getUsed_layout().equals("2")){
                holder.easyFlipView.setVisibility(View.VISIBLE);
                holder.easyFlipView2.setVisibility(View.GONE);
                holder.easyFlipView3.setVisibility(View.GONE);
                holder.easyFlipView4.setVisibility(View.GONE);
                holder.easyFlipView1.setVisibility(View.GONE);
                holder.easyFlipView5.setVisibility(View.GONE);
            }
            if (savedUserDetailModelClass.getUsed_layout().equals("3")){
                holder.easyFlipView.setVisibility(View.GONE);
                holder.easyFlipView2.setVisibility(View.GONE);
                holder.easyFlipView3.setVisibility(View.GONE);
                holder.easyFlipView4.setVisibility(View.GONE);
                holder.easyFlipView1.setVisibility(View.VISIBLE);
                holder.easyFlipView5.setVisibility(View.GONE);
            }

            if (savedUserDetailModelClass.getUsed_layout().equals("4")){
                holder.easyFlipView.setVisibility(View.GONE);
                holder.easyFlipView2.setVisibility(View.VISIBLE);
                holder.easyFlipView3.setVisibility(View.GONE);
                holder.easyFlipView4.setVisibility(View.GONE);
                holder.easyFlipView1.setVisibility(View.GONE);
                holder.easyFlipView5.setVisibility(View.GONE);
            }

            if (savedUserDetailModelClass.getUsed_layout().equals("5")){
                holder.easyFlipView.setVisibility(View.GONE);
                holder.easyFlipView2.setVisibility(View.GONE);
                holder.easyFlipView3.setVisibility(View.VISIBLE);
                holder.easyFlipView4.setVisibility(View.GONE);
                holder.easyFlipView1.setVisibility(View.GONE);
                holder.easyFlipView5.setVisibility(View.GONE);
            }

            if (savedUserDetailModelClass.getUsed_layout().equals("6")){
                holder.easyFlipView.setVisibility(View.GONE);
                holder.easyFlipView2.setVisibility(View.GONE);
                holder.easyFlipView3.setVisibility(View.GONE);
                holder.easyFlipView4.setVisibility(View.VISIBLE);
                holder.easyFlipView1.setVisibility(View.GONE);
                holder.easyFlipView5.setVisibility(View.GONE);
            }

            if (savedUserDetailModelClass.getUsed_layout().equals("7")){
                holder.easyFlipView.setVisibility(View.GONE);
                holder.easyFlipView2.setVisibility(View.GONE);
                holder.easyFlipView3.setVisibility(View.GONE);
                holder.easyFlipView4.setVisibility(View.GONE);
                holder.easyFlipView1.setVisibility(View.GONE);
                holder.easyFlipView5.setVisibility(View.GONE);
            }

            String faxNo = savedUserDetailModelClass.getFaxNo();
            String poBoxNo = savedUserDetailModelClass.getPoBoxNo();

            if (faxNo.equals("0"))
            {
                holder.txtFax1.setVisibility(View.GONE);
                holder.txtFax2.setVisibility(View.GONE);
                holder.txtFax3.setVisibility(View.GONE);
                holder.txtFax4.setVisibility(View.GONE);
                holder.txtFax5.setVisibility(View.GONE);
                holder.txtFax6.setVisibility(View.GONE);
            }else{
                holder.txtFax1.setText("Fax No. : "+faxNo);
                holder.txtFax2.setText("Fax No. : "+faxNo);
                holder.txtFax3.setText("Fax No. : "+faxNo);
                holder.txtFax4.setText("Fax No. : "+faxNo);
                holder.txtFax5.setText("Fax No. : "+faxNo);
                holder.txtFax6.setText("Fax No. : "+faxNo);
            }

            if (poBoxNo.equals("0"))
            {
                holder.txtPo1.setVisibility(View.GONE);
                holder.txtPo2.setVisibility(View.GONE);
                holder.txtPo3.setVisibility(View.GONE);
                holder.txtPo4.setVisibility(View.GONE);
                holder.txtPo5.setVisibility(View.GONE);
                holder.txtPo6.setVisibility(View.GONE);
            }else{
                holder.txtPo1.setText("Po Box No. : "+poBoxNo);
                holder.txtPo2.setText("Po Box No. : "+poBoxNo);
                holder.txtPo3.setText("Po Box No. : "+poBoxNo);
                holder.txtPo4.setText("Po Box No. : "+poBoxNo);
                holder.txtPo5.setText("Po Box No. : "+poBoxNo);
                holder.txtPo6.setText("Po Box No. : "+poBoxNo);
            }


            String colorCode = savedUserDetailModelClass.getColorCode();

            holder.txtPos.setText(savedUserDetailModelClass.getPosition());
            holder.txtName.setText(savedUserDetailModelClass.getName());
            holder.txtAddress.setText(savedUserDetailModelClass.getAddress());
            holder.txtPh.setText(savedUserDetailModelClass.getPhone());
            holder.txtWeb.setText(savedUserDetailModelClass.getWeb());
            holder.txtEmail.setText(savedUserDetailModelClass.getEmail());

            holder.txtPos1.setText(savedUserDetailModelClass.getPosition());
            holder.txtName1.setText(savedUserDetailModelClass.getName());
            holder.txtAddress1.setText(savedUserDetailModelClass.getAddress());
            holder.txtPh1.setText(savedUserDetailModelClass.getPhone());
            holder.txtWeb1.setText(savedUserDetailModelClass.getWeb());
            holder.txtEmail1.setText(savedUserDetailModelClass.getEmail());

            holder.txtPos2.setText(savedUserDetailModelClass.getPosition());
            holder.txtName2.setText(savedUserDetailModelClass.getName());
            holder.txtAddress2.setText(savedUserDetailModelClass.getAddress());
            holder.txtPh2.setText(savedUserDetailModelClass.getPhone());
            holder.txtWeb2.setText(savedUserDetailModelClass.getWeb());
            holder.txtEmail2.setText(savedUserDetailModelClass.getEmail());

            holder.txtPos3.setText(savedUserDetailModelClass.getPosition());
            holder.txtName3.setText(savedUserDetailModelClass.getName());
            holder.txtAddress3.setText(savedUserDetailModelClass.getAddress());
            holder.txtPh3.setText(savedUserDetailModelClass.getPhone());
            holder.txtWeb3.setText(savedUserDetailModelClass.getWeb());
            holder.txtEmail3.setText(savedUserDetailModelClass.getEmail());

            holder.txtPos4.setText(savedUserDetailModelClass.getPosition());
            holder.txtName4.setText(savedUserDetailModelClass.getName());
            holder.txtAddress4.setText(savedUserDetailModelClass.getAddress());
            holder.txtPh4.setText(savedUserDetailModelClass.getPhone());
            holder.txtWeb4.setText(savedUserDetailModelClass.getWeb());
            holder.txtEmail4.setText(savedUserDetailModelClass.getEmail());

            holder.txtPos5.setText(savedUserDetailModelClass.getPosition());
            holder.txtName5.setText(savedUserDetailModelClass.getName());
            holder.txtAddress5.setText(savedUserDetailModelClass.getAddress());
            holder.txtPh5.setText(savedUserDetailModelClass.getPhone());
            holder.txtWeb5.setText(savedUserDetailModelClass.getWeb());
            holder.txtEmail5.setText(savedUserDetailModelClass.getEmail());

            holder.txtPos6.setText(savedUserDetailModelClass.getPosition());
            holder.txtName6.setText(savedUserDetailModelClass.getName());
            holder.txtAddress6.setText(savedUserDetailModelClass.getAddress());
            holder.txtPh6.setText(savedUserDetailModelClass.getPhone());
            holder.txtWeb6.setText(savedUserDetailModelClass.getWeb());
            holder.txtEmail6.setText(savedUserDetailModelClass.getEmail());

            holder.txtCompany1.setText(savedUserDetailModelClass.getCompany());
            holder.txtCompany2.setText(savedUserDetailModelClass.getCompany());
            holder.txtCompany3.setText(savedUserDetailModelClass.getCompany());
            holder.txtCompany4.setText(savedUserDetailModelClass.getCompany());
            holder.txtCompany5.setText(savedUserDetailModelClass.getCompany());
            holder.txtCompany6.setText(savedUserDetailModelClass.getCompany());
            holder.txtCompany7.setText(savedUserDetailModelClass.getCompany());

            holder.txtPh.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtWeb.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtEmail.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtAddress.setTextColor(Color.parseColor("#"+colorCode));
            holder.txtPh1.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtWeb1.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtEmail1.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtAddress1.setTextColor(Color.parseColor("#"+colorCode));
            holder.txtPh2.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtWeb2.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtEmail2.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtAddress2.setTextColor(Color.parseColor("#"+colorCode));
            holder.txtPh3.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtWeb3.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtEmail3.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtAddress3.setTextColor(Color.parseColor("#"+colorCode));
            holder.txtPh4.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtWeb4.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtEmail4.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtAddress4.setTextColor(Color.parseColor("#"+colorCode));
            holder.txtPh5.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtWeb5.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtEmail5.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtAddress5.setTextColor(Color.parseColor("#"+colorCode));
            holder.txtPh6.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtWeb6.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtEmail6.setLinkTextColor(Color.parseColor("#"+colorCode));
            holder.txtAddress6.setTextColor(Color.parseColor("#"+colorCode));

            holder.txtFax5.setBackgroundColor(Color.parseColor("#"+colorCode));

            holder.conHalfView.setBackgroundColor(Color.parseColor("#"+colorCode));
            holder.conUpDownView.setBackgroundColor(Color.parseColor("#"+colorCode));
            holder.conSideView.setBackgroundColor(Color.parseColor("#"+colorCode));
            holder.conRightView.setBackgroundColor(Color.parseColor("#"+colorCode));
            holder.conHalfCurveView.setBackgroundColor(Color.parseColor("#"+colorCode));
            holder.conCurView.setBackgroundColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable = (GradientDrawable) holder.phImg.getBackground();
            drawable.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable1 = (GradientDrawable) holder.webImg.getBackground();
            drawable1.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable2 = (GradientDrawable) holder.emailImg.getBackground();
            drawable2.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable3 = (GradientDrawable) holder.locImg.getBackground();
            drawable3.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable4 = (GradientDrawable) holder.phImg1.getBackground();
            drawable4.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable5 = (GradientDrawable) holder.webImg1.getBackground();
            drawable5.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable6 = (GradientDrawable) holder.emailImg1.getBackground();
            drawable6.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable7 = (GradientDrawable) holder.locImg1.getBackground();
            drawable7.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable8 = (GradientDrawable) holder.phImg2.getBackground();
            drawable8.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable9 = (GradientDrawable) holder.webImg2.getBackground();
            drawable9.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable10 = (GradientDrawable) holder.emailImg2.getBackground();
            drawable10.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable11 = (GradientDrawable) holder.locImg2.getBackground();
            drawable11.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable12 = (GradientDrawable) holder.phImg3.getBackground();
            drawable12.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable13 = (GradientDrawable) holder.webImg3.getBackground();
            drawable13.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable14 = (GradientDrawable) holder.emailImg3.getBackground();
            drawable14.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable15 = (GradientDrawable) holder.locImg3.getBackground();
            drawable15.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable16 = (GradientDrawable) holder.phImg4.getBackground();
            drawable16.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable17 = (GradientDrawable) holder.webImg4.getBackground();
            drawable17.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable18 = (GradientDrawable) holder.emailImg4.getBackground();
            drawable18.setColor(Color.parseColor("#"+colorCode));

            GradientDrawable drawable19 = (GradientDrawable) holder.locImg4.getBackground();
            drawable19.setColor(Color.parseColor("#"+colorCode));

            File pictureFileDir1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ".ezivizi");
            String filename1 = pictureFileDir1.getPath() +File.separator+ Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)+".jpg";


            Glide.with(getActivity()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img);
            Glide.with(getActivity()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img1);
            Glide.with(getActivity()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img2);
            Glide.with(getActivity()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img3);
            Glide.with(getActivity()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img4);
            Glide.with(getActivity()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img5);
            Glide.with(getActivity()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img6);


            holder.easyFlipView.setFlipDuration(1000);
            holder.easyFlipView.setFlipEnabled(true);
            holder.easyFlipView1.setFlipDuration(1000);
            holder.easyFlipView1.setFlipEnabled(true);
            holder.easyFlipView2.setFlipDuration(1000);
            holder.easyFlipView2.setFlipEnabled(true);
            holder.easyFlipView3.setFlipDuration(1000);
            holder.easyFlipView3.setFlipEnabled(true);
            holder.easyFlipView5.setFlipDuration(1000);
            holder.easyFlipView5.setFlipEnabled(true);
            holder.easyFlipView4.setFlipDuration(1000);
            holder.easyFlipView4.setFlipEnabled(true);

            holder.sideView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.easyFlipView.flipTheView();
                }
            });

            holder.halfView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.easyFlipView5.flipTheView();
                }
            });

            holder.curveView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.easyFlipView1.flipTheView();
                }
            });

            holder.halfCurveView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.easyFlipView2.flipTheView();
                }
            });

            holder.up_downView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.easyFlipView3.flipTheView();
                }
            });

            holder.rightView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.easyFlipView4.flipTheView();
                }
            });

            holder.flip_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.easyFlipView.flipTheView();
                }
            });

            holder.flip_back1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.easyFlipView1.flipTheView();
                }
            });

            holder.flip_back2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.easyFlipView2.flipTheView();
                }
            });

            holder.flip_back3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.easyFlipView3.flipTheView();
                }
            });

            holder.flip_back4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.easyFlipView4.flipTheView();
                }
            });

            holder.flip_back5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.easyFlipView5.flipTheView();
                }
            });



            holder.bckImg.setBackgroundColor(Color.parseColor("#"+colorCode));
            holder.bckImg1.setBackgroundColor(Color.parseColor("#"+colorCode));
            holder.bckImg2.setBackgroundColor(Color.parseColor("#"+colorCode));
            holder.bckImg3.setBackgroundColor(Color.parseColor("#"+colorCode));
            holder.bckImg4.setBackgroundColor(Color.parseColor("#"+colorCode));
            holder.bckImg5.setBackgroundColor(Color.parseColor("#"+colorCode));


        }

        @Override
        public int getItemCount() {
            return savedUserDetailModelClassList.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    List<SavedUserDetailModelClass> filteredResults = new ArrayList<>();
                    if (constraint.length() == 0) {
                        filteredResults.addAll(filtersavedUserDetailModelClassList);
                    } else {
                        filteredResults = getFilteredResults(constraint.toString().toLowerCase().trim());
                    }

                    FilterResults results = new FilterResults();
                    results.values = filteredResults;


                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    savedUserDetailModelClassList.clear();
                    savedUserDetailModelClassList.addAll((List)results.values);
                    cursor.requery();
                    notifyDataSetChanged();
                }
            };
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView txtFax1,txtFax2,txtFax3,txtFax4,txtFax5,txtFax6,txtPo1,txtPo2,txtPo3,txtPo4,txtPo5,txtPo6;

            String colorCode;
            CardView semiView;
            Button btnDone;
            QRGenerateActivity qrGenerateActivity;
            ImageView phImg, locImg, webImg, emailImg, phImg1, locImg1, webImg1, emailImg1, phImg2, locImg2, webImg2, emailImg2;
            TextView txtPh, txtEmail, txtWeb, txtAddress, txtPh1, txtEmail1, txtWeb1, txtAddress1, txtPh2, txtEmail2, txtWeb2, txtAddress2, txtName, txtName1, txtName2, txtPos, txtPos1, txtPos2;
            RadioButton rbSemi, rbHalf, rbRight, rbCurve, rbHalfCurve, rbSide, rbUpdown;
            RadioGroup radioGroup1;
            String usedLayout = "0";
            ImageView view12;
            ImageView phImg3, locImg3, webImg3, emailImg3, phImg4, locImg4, webImg4, emailImg4;
            TextView txtPh3, txtEmail3, txtWeb3, txtAddress3, txtPh4, txtEmail4, txtWeb4, txtAddress4, txtPh5, txtEmail5, txtWeb5, txtAddress5, txtPh6, txtEmail6, txtWeb6, txtAddress6;
            TextView txtName3, txtPos3, txtName4, txtPos4, txtName5, txtPos5, txtName6, txtPos6;
            CircleImageView img3, img4, img5, img6, img, img1, img2;
            TextView txtCompany1, txtCompany2, txtCompany3, txtCompany4, txtCompany5, txtCompany6,txtCompany7;

            RelativeLayout sideView;
            RelativeLayout halfView, rightView;
            RelativeLayout curveView, halfCurveView, up_downView;
            EasyFlipView easyFlipView,easyFlipView1,easyFlipView2,easyFlipView3,easyFlipView4,easyFlipView5;
            ConstraintLayout conHalfView,conRightView,conSideView,conCurView,conHalfCurveView,conUpDownView;
            ImageView bckImg,bckImg1,bckImg2,bckImg3,bckImg4,bckImg5;
            RecyclerView listView,listView1,listView2,listView3,listView4,listView5;
            RelativeLayout flip_back,flip_back1,flip_back2,flip_back3,flip_back4,flip_back5;


            public MyViewHolder(@NonNull View view) {
                super(view);

                halfView = view.findViewById(R.id.halfView);
                semiView = view.findViewById(R.id.semiView);
                rightView = view.findViewById(R.id.rightView);
                curveView = view.findViewById(R.id.curveView);
                sideView = view.findViewById(R.id.sideView);
                halfCurveView = view.findViewById(R.id.halfCurveView);
                up_downView = view.findViewById(R.id.UpDownView);


                easyFlipView = view.findViewById(R.id.cardFlipView);
                easyFlipView1 = view.findViewById(R.id.cardFlipView1);
                easyFlipView2 = view.findViewById(R.id.cardFlipView2);
                easyFlipView3 = view.findViewById(R.id.cardFlipView3);
                easyFlipView4 = view.findViewById(R.id.cardFlipView4);
                easyFlipView5 = view.findViewById(R.id.cardFlipView5);



                flip_back = view.findViewById(R.id.flip_back);
                flip_back1 = view.findViewById(R.id.flip_back1);
                flip_back2 = view.findViewById(R.id.flip_back2);
                flip_back3 = view.findViewById(R.id.flip_back3);
                flip_back4 = view.findViewById(R.id.flip_back4);
                flip_back5 = view.findViewById(R.id.flip_back5);




                listView = view.findViewById(R.id.listView);
                listView1 = view.findViewById(R.id.listView1);
                listView2= view.findViewById(R.id.listView2);
                listView3 = view.findViewById(R.id.listView3);
                listView4 = view.findViewById(R.id.listView4);
                listView5 = view.findViewById(R.id.listView5);


                bckImg = view.findViewById(R.id.bckimg);
                bckImg1 = view.findViewById(R.id.bckimg1);
                bckImg2 = view.findViewById(R.id.bckimg2);
                bckImg3 = view.findViewById(R.id.bckimg3);
                bckImg4 = view.findViewById(R.id.bckimg4);
                bckImg5= view.findViewById(R.id.bckimg5);

                conCurView = view.findViewById(R.id.conCurveView);
                conHalfCurveView = view.findViewById(R.id.conHalfCurveView);
                conHalfView = view.findViewById(R.id.conHalfView);
                conRightView = view.findViewById(R.id.conRightView);
                conSideView = view.findViewById(R.id.consSideView);
                conUpDownView = view.findViewById(R.id.conUpDownView);


                txtFax1 = view.findViewById(R.id.txtFax1);
                txtFax2 = view.findViewById(R.id.txtFax2);
                txtFax3 = view.findViewById(R.id.txtFax3);
                txtFax4 = view.findViewById(R.id.txtFax4);
                txtFax5 = view.findViewById(R.id.txtFax5);
                txtFax6 = view.findViewById(R.id.txtFax6);

                txtPo1 = view.findViewById(R.id.txtPo1);
                txtPo2 = view.findViewById(R.id.txtPo2);
                txtPo3 = view.findViewById(R.id.txtPo3);
                txtPo4 = view.findViewById(R.id.txtPo4);
                txtPo5 = view.findViewById(R.id.txtPo5);
                txtPo6 = view.findViewById(R.id.txtPo6);


        /*
         image
        */
                phImg = view.findViewById(R.id.phImg);
                locImg = view.findViewById(R.id.locImg);
                webImg = view.findViewById(R.id.webImg);
                emailImg = view.findViewById(R.id.emailImg);
                phImg1 = view.findViewById(R.id.phImg1);
                locImg1 = view.findViewById(R.id.locImg1);
                webImg1 = view.findViewById(R.id.webImg1);
                emailImg1 = view.findViewById(R.id.emailImg1);
                phImg2 = view.findViewById(R.id.phImg2);
                locImg2 = view.findViewById(R.id.locImg2);
                webImg2 = view.findViewById(R.id.webImg2);
                emailImg2 = view.findViewById(R.id.emailImg2);
                locImg3 = view.findViewById(R.id.imageView6);
                locImg4 = view.findViewById(R.id.imageView11);
                webImg3 = view.findViewById(R.id.imageView8);
                webImg4 = view.findViewById(R.id.imageView13);
                emailImg3 = view.findViewById(R.id.imageView9);
                emailImg4 = view.findViewById(R.id.imageView14);
                phImg3 = view.findViewById(R.id.imageView7);
                phImg4 = view.findViewById(R.id.imageView12);
                img = view.findViewById(R.id.imageView);
                img1 = view.findViewById(R.id.imageView1);
                img2 = view.findViewById(R.id.imageView3);
                img3 = view.findViewById(R.id.imageView4);
                img4 = view.findViewById(R.id.imgLogo1);
                img5 = view.findViewById(R.id.imgLogo2);
                img6 = view.findViewById(R.id.imgLogo3);


        /*
        company text view
         */
                txtCompany1 = view.findViewById(R.id.txtCompanyName1);
                txtCompany3 = view.findViewById(R.id.txtCompanyName3);
                txtCompany2 = view.findViewById(R.id.textView18);
                txtCompany4 = view.findViewById(R.id.txtCompanyName2);
                txtCompany5 = view.findViewById(R.id.textView17);
                txtCompany6 = view.findViewById(R.id.textView19);
                txtCompany7 = view.findViewById(R.id.textView12);

        /*
            text
        */
                txtPh = view.findViewById(R.id.txtPh);
                txtPh1 = view.findViewById(R.id.txtPh1);
                txtPh2 = view.findViewById(R.id.txtPh2);
                txtPh3 = view.findViewById(R.id.txtPh3);
                txtPh4 = view.findViewById(R.id.txtPh4);
                txtPh5 = view.findViewById(R.id.txtPh5);
                txtPh6 = view.findViewById(R.id.textView8);

                txtWeb = view.findViewById(R.id.txtWeb);
                txtWeb1 = view.findViewById(R.id.txtWeb1);
                txtWeb2 = view.findViewById(R.id.txtWeb2);
                txtWeb3 = view.findViewById(R.id.txtWeb3);
                txtWeb4 = view.findViewById(R.id.txtWeb4);
                txtWeb5 = view.findViewById(R.id.txtWeb5);
                txtWeb6 = view.findViewById(R.id.textView10);

                txtEmail = view.findViewById(R.id.txtEmail);
                txtEmail1 = view.findViewById(R.id.txtEmail1);
                txtEmail2 = view.findViewById(R.id.txtEmail2);
                txtEmail3 = view.findViewById(R.id.txtEmail3);
                txtEmail4 = view.findViewById(R.id.txtEmail4);
                txtEmail5 = view.findViewById(R.id.txtEmail5);
                txtEmail6 = view.findViewById(R.id.textView9);

                txtAddress = view.findViewById(R.id.txtAddress);
                txtAddress1 = view.findViewById(R.id.txtAddress1);
                txtAddress2 = view.findViewById(R.id.txtAddress2);
                txtAddress6 = view.findViewById(R.id.textView7);
                txtAddress5 = view.findViewById(R.id.txtAddress5);
                txtAddress3 = view.findViewById(R.id.txtAddress3);
                txtAddress4 = view.findViewById(R.id.txtAddress4);

        /*
        username
        */
                txtName = view.findViewById(R.id.txtUserName);
                txtName1 = view.findViewById(R.id.textView5);
                txtName2 = view.findViewById(R.id.textView);
                txtName3 = view.findViewById(R.id.txtUserName1);
                txtName4 = view.findViewById(R.id.txtUserName2);
                txtName5 = view.findViewById(R.id.txtUserName3);
                txtName6 = view.findViewById(R.id.textView11);

        /*
        position
         */
                txtPos = view.findViewById(R.id.textView6);
                txtPos1 = view.findViewById(R.id.txtPosition);
                txtPos2 = view.findViewById(R.id.textView2);
                txtPos3 = view.findViewById(R.id.txtPosition1);
                txtPos4 = view.findViewById(R.id.textView13);
                txtPos5 = view.findViewById(R.id.txtPosition2);
                txtPos6 = view.findViewById(R.id.txtPosition3);


            }
        }

        private List<SavedUserDetailModelClass> getFilteredResults(String constraint) {
            List<SavedUserDetailModelClass> results = new ArrayList<>();
            for (SavedUserDetailModelClass item : filtersavedUserDetailModelClassList) {
                if (item.getName().toLowerCase().contains(constraint)) {
                    results.add(item);
                }
            }
            return results;
        }


    }

    public class ServiceAdapterClass extends RecyclerView.Adapter<ServiceAdapterClass.MyViewHolder>{

        List<ServicesModelClass> list;

        public ServiceAdapterClass(List<ServicesModelClass> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ServiceAdapterClass.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_item,parent,false);

            return new ServiceAdapterClass.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ServiceAdapterClass.MyViewHolder holder, int position) {

            ServicesModelClass servicesModelClass = list.get(position);
            if (servicesModelClass.getService6().equals("no service")){
                holder.txtView6.setVisibility(View.GONE);
            }

            if (servicesModelClass.getService1().equals("no service")){
                holder.txtView1.setVisibility(View.GONE);
            }

            if (servicesModelClass.getService2().equals("no service")){
                holder.txtView2.setVisibility(View.GONE);
            }

            if (servicesModelClass.getService3().equals("no service")){
                holder.txtView3.setVisibility(View.GONE);
            }

            if (servicesModelClass.getService4().equals("no service")){
                holder.txtView4.setVisibility(View.GONE);
            }

            if (servicesModelClass.getService5().equals("no service")){
                holder.txtView5.setVisibility(View.GONE);
            }

            holder.txtView1.setText("1"+") "+servicesModelClass.getService1());
            holder.txtView2.setText("2"+") "+servicesModelClass.getService2());
            holder.txtView3.setText("3"+") "+servicesModelClass.getService3());
            holder.txtView4.setText("4"+") "+servicesModelClass.getService4());
            holder.txtView5.setText("5"+") "+servicesModelClass.getService5());
            holder.txtView6.setText("6"+") "+servicesModelClass.getService6());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView txtView1,txtView2,txtView3,txtView4,txtView5,txtView6;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                txtView1 = itemView.findViewById(R.id.txtView1);
                txtView2 = itemView.findViewById(R.id.txtView2);
                txtView3 = itemView.findViewById(R.id.txtView3);
                txtView4 = itemView.findViewById(R.id.txtView4);
                txtView5 = itemView.findViewById(R.id.txtView5);
                txtView6 = itemView.findViewById(R.id.txtView6);
            }
        }
    }

}
