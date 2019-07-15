package com.kandktech.ezivizi.fragments;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kandktech.ezivizi.DbHandler;
import com.kandktech.ezivizi.QRGenerateActivity;
import com.kandktech.ezivizi.R;
import com.kandktech.ezivizi.model_class.SavedUserDetailModelClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class ListFragment extends Fragment {

    View view;
    RecyclerView listView;
    FloatingTextButton fab;
    DbHandler dbHandler;
//    CardCursorAdapter cd;

    UserAdapterClass adapterClass;
    Cursor cursor = null;

    List<SavedUserDetailModelClass> userDetailModelClasses;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list, container, false);
        listView = view.findViewById(R.id.listView);
        fab = view.findViewById(R.id.action_button);
        if (getActivity() != null) dbHandler = new DbHandler(getActivity());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), QRGenerateActivity.class));
            }
        });

        setHasOptionsMenu(true);

        userDetailModelClasses = new ArrayList<>();

        try {
            cursor = dbHandler.viewData();
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


                       userDetailModelClasses.add(userDetail);

                    } while (cursor.moveToNext());

                }

//                String[] from = {DbHandler.user_name, DbHandler.user_address,DbHandler.user_email,DbHandler.user_phone,DbHandler.user_position,DbHandler.user_website,DbHandler.user_logo};
//                int[] to = {R.id.textView, R.id.txtAddress,R.id.txtEmail,R.id.txtPh,R.id.textView2,R.id.txtWeb,R.id.imageView};
//                cd = new CardCursorAdapter(getActivity(), R.layout.right_view_layout, cursor, from, to, 0);
//                listView.setAdapter(cd);
//
//                String[] from1 = {DbHandler.user_name, DbHandler.user_address,DbHandler.user_email,DbHandler.user_phone,DbHandler.user_position,DbHandler.user_website,DbHandler.user_logo};
//                int[] to1 = {R.id.textView5, R.id.txtAddress2,R.id.txtEmail2,R.id.txtPh2,R.id.textView6,R.id.txtWeb2,R.id.imageView3};
//                cd = new CardCursorAdapter(getActivity(), R.layout.right_view_layout, cursor, from1, to1, 0);
//                listView.setAdapter(cd);
//
//                String[] from2 = {DbHandler.user_name, DbHandler.user_address,DbHandler.user_email,DbHandler.user_phone,DbHandler.user_position,DbHandler.user_website,DbHandler.user_logo};
//                int[] to2 = {R.id.textView1, R.id.txtAddress1,R.id.txtEmail1,R.id.txtPh1,R.id.textView12,R.id.txtWeb1,R.id.imageView1};
//                cd = new CardCursorAdapter(getActivity(), R.layout.right_view_layout, cursor, from2, to2, 0);
//                listView.setAdapter(cd);


                adapterClass = new UserAdapterClass(getActivity(),userDetailModelClasses);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                listView.setLayoutManager(mLayoutManager);
                listView.setItemAnimator(new DefaultItemAnimator());
                listView.setHasFixedSize(true);
                listView.setAdapter(adapterClass);
//
                cursor.requery();
                adapterClass.notifyDataSetChanged();
//                cd1.notifyDataSetChanged();
//                cd2.notifyDataSetChanged();

//                cd.setFilterQueryProvider(new FilterQueryProvider() {
//                    public Cursor runQuery(CharSequence constraint) {
//                        return getCursor(constraint.toString());
//                    }
//                });

            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return view;
    }

    public class UserAdapterClass extends RecyclerView.Adapter<UserAdapterClass.MyViewHolder> implements Filterable {

        Context context;
        List<SavedUserDetailModelClass> savedUserDetailModelClassList;
        List<SavedUserDetailModelClass> filtersavedUserDetailModelClassList;

        public UserAdapterClass(Context context, List<SavedUserDetailModelClass> savedUserDetailModelClassList) {
            this.context = context;
            this.savedUserDetailModelClassList = savedUserDetailModelClassList;
            filtersavedUserDetailModelClassList = new ArrayList<>(savedUserDetailModelClassList);
        }


        @NonNull
        @Override
        public UserAdapterClass.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.right_view_layout,viewGroup,false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserAdapterClass.MyViewHolder holder, int i) {

            SavedUserDetailModelClass savedUserDetailModelClass = savedUserDetailModelClassList.get(i);

            if (savedUserDetailModelClass.getUsed_layout().equals("1")){
                holder.rightView.setVisibility(View.VISIBLE);
                holder.semiView.setVisibility(View.GONE);
                holder.halfView.setVisibility(View.GONE);
            }
            if (savedUserDetailModelClass.getUsed_layout().equals("2")){
                holder.semiView.setVisibility(View.VISIBLE);
                holder.rightView.setVisibility(View.GONE);
                holder.halfView.setVisibility(View.GONE);
            }
            if (savedUserDetailModelClass.getUsed_layout().equals("3")){
                holder.halfView.setVisibility(View.VISIBLE);
                holder.rightView.setVisibility(View.GONE);
                holder.semiView.setVisibility(View.GONE);
            }

            String colorCode = savedUserDetailModelClass.getColorCode();


            GradientDrawable drawable = (GradientDrawable) holder.phImg.getBackground();
            drawable.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable1 = (GradientDrawable) holder.webImg.getBackground();
            drawable1.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable2 = (GradientDrawable) holder.emailImg.getBackground();
            drawable2.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable3 = (GradientDrawable) holder.locImg.getBackground();
            drawable3.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable4 = (GradientDrawable) holder.phImg1.getBackground();
            drawable4.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable5 = (GradientDrawable) holder.webImg1.getBackground();
            drawable5.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable6 = (GradientDrawable) holder.emailImg1.getBackground();
            drawable6.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable7 = (GradientDrawable) holder.locImg1.getBackground();
            drawable7.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable8 = (GradientDrawable) holder.phImg2.getBackground();
            drawable8.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable9 = (GradientDrawable) holder.webImg2.getBackground();
            drawable9.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable10 = (GradientDrawable) holder.emailImg2.getBackground();
            drawable10.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable11 = (GradientDrawable) holder.locImg2.getBackground();
            drawable11.setColor(Integer.parseInt(colorCode));

            holder.txtName.setText(savedUserDetailModelClass.getName());
            holder.txtEmail.setText(savedUserDetailModelClass.getEmail());
            holder.txtWeb.setText(savedUserDetailModelClass.getWeb());
            holder.txtAddress.setText(savedUserDetailModelClass.getAddress());
            holder.txtPos.setText(savedUserDetailModelClass.getPosition());
            holder.txtPh.setText(savedUserDetailModelClass.getPhone());

            holder.txtName1.setText(savedUserDetailModelClass.getName());
            holder.txtEmail1.setText(savedUserDetailModelClass.getEmail());
            holder.txtWeb1.setText(savedUserDetailModelClass.getWeb());
            holder.txtAddress1.setText(savedUserDetailModelClass.getAddress());
            holder.txtPos1.setText(savedUserDetailModelClass.getPosition());
            holder.txtPh1.setText(savedUserDetailModelClass.getPhone());

            holder.txtName2.setText(savedUserDetailModelClass.getName());
            holder.txtEmail2.setText(savedUserDetailModelClass.getEmail());
            holder.txtWeb2.setText(savedUserDetailModelClass.getWeb());
            holder.txtAddress2.setText(savedUserDetailModelClass.getAddress());
            holder.txtPos2.setText(savedUserDetailModelClass.getPosition());
            holder.txtPh2.setText(savedUserDetailModelClass.getPhone());

            holder.txtPh.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtWeb.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtEmail.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtAddress.setTextColor(Integer.parseInt(colorCode));
            holder.txtPh1.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtWeb1.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtEmail1.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtAddress1.setTextColor(Integer.parseInt(colorCode));
            holder.txtPh2.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtWeb2.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtEmail2.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtAddress2.setTextColor(Integer.parseInt(colorCode));

            File pictureFileDir1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ".ezivizi");
            String filename1 = pictureFileDir1.getPath() +File.separator+ Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)+".jpg";

            holder.halfView.setCardBackgroundColor(Integer.parseInt(colorCode));
            holder.semiView.setCardBackgroundColor(Integer.parseInt(colorCode));
            holder.rightView.setCardBackgroundColor(Integer.parseInt(colorCode));
//
//            if (savedUserDetailModelClass.getUser_logo().equals(filename1)){
//                System.out.println("file : "+filename1);
//                Glide.with(getActivity()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img);
//                Glide.with(getActivity()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img1);
//                Glide.with(getActivity()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img2);
//            }else{
//                File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ".ezivizi");
//                String filename = pictureFileDir.getPath() +File.separator+savedUserDetailModelClass.getUser_logo();

                Glide.with(getActivity()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img);
                Glide.with(getActivity()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img1);
                Glide.with(getActivity()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img2);
//            }


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

            CardView halfView, rightView, semiView;
            ImageView phImg, locImg, webImg, emailImg, phImg1, locImg1, webImg1, emailImg1, phImg2, locImg2, webImg2, emailImg2, img, img1, img2;
            TextView txtPh, txtEmail, txtWeb, txtAddress, txtPh1, txtEmail1, txtWeb1, txtAddress1, txtPh2, txtEmail2, txtWeb2, txtAddress2, txtName, txtName1, txtName2, txtPos, txtPos1, txtPos2;


            public MyViewHolder(@NonNull View view) {
                super(view);

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
                img = view.findViewById(R.id.imageView);
                img1 = view.findViewById(R.id.imageView1);
                img2 = view.findViewById(R.id.imageView3);


            /*
               text
            */
                txtPh = view.findViewById(R.id.txtPh);
                txtAddress = view.findViewById(R.id.txtAddress);
                txtEmail = view.findViewById(R.id.txtEmail);
                txtWeb = view.findViewById(R.id.txtWeb);
                txtPh1 = view.findViewById(R.id.txtPh1);
                txtAddress1 = view.findViewById(R.id.txtAddress1);
                txtEmail1 = view.findViewById(R.id.txtEmail1);
                txtWeb1 = view.findViewById(R.id.txtWeb1);
                txtPh2 = view.findViewById(R.id.txtPh2);
                txtAddress2 = view.findViewById(R.id.txtAddress2);
                txtEmail2 = view.findViewById(R.id.txtEmail2);
                txtWeb2 = view.findViewById(R.id.txtWeb2);
                txtName = view.findViewById(R.id.textView);
                txtName1 = view.findViewById(R.id.textView5);
                txtName2 = view.findViewById(R.id.textView1);
                txtPos = view.findViewById(R.id.textView6);
                txtPos1 = view.findViewById(R.id.textView12);
                txtPos2 = view.findViewById(R.id.textView2);

            /*

                views

             */
                halfView = view.findViewById(R.id.halfView);
                semiView = view.findViewById(R.id.semiView);
                rightView = view.findViewById(R.id.rightView);
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



//    public class CardCursorAdapter extends SimpleCursorAdapter {
//
//        public CardCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
//            super(context, layout, c, from, to, flags);
//        }
//
//        @Override
//        public View newView(Context context, Cursor cursor, ViewGroup parent) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//            View view = inflater.inflate(R.layout.right_view_layout, parent, false);
//
//            ViewHolder holder = new ViewHolder();
//            view.setTag(holder);
//
//
//            /*
//                image
//            */
//            holder.phImg = view.findViewById(R.id.phImg);
//            holder.locImg = view.findViewById(R.id.locImg);
//            holder.webImg = view.findViewById(R.id.webImg);
//            holder.emailImg = view.findViewById(R.id.emailImg);
//            holder.phImg1 = view.findViewById(R.id.phImg1);
//            holder.locImg1 = view.findViewById(R.id.locImg1);
//            holder.webImg1 = view.findViewById(R.id.webImg1);
//            holder.emailImg1 = view.findViewById(R.id.emailImg1);
//            holder.phImg2 = view.findViewById(R.id.phImg2);
//            holder.locImg2 = view.findViewById(R.id.locImg2);
//            holder.webImg2 = view.findViewById(R.id.webImg2);
//            holder.emailImg2 = view.findViewById(R.id.emailImg2);
//            holder.img = view.findViewById(R.id.imageView);
//            holder.img1 = view.findViewById(R.id.imageView1);
//            holder.img2 = view.findViewById(R.id.imageView3);
//
//
//            /*
//               text
//            */
//            holder.txtPh = view.findViewById(R.id.txtPh);
//            holder.txtAddress = view.findViewById(R.id.txtAddress);
//            holder.txtEmail = view.findViewById(R.id.txtEmail);
//            holder.txtWeb = view.findViewById(R.id.txtWeb);
//            holder.txtPh1 = view.findViewById(R.id.txtPh1);
//            holder.txtAddress1 = view.findViewById(R.id.txtAddress1);
//            holder.txtEmail1 = view.findViewById(R.id.txtEmail1);
//            holder.txtWeb1 = view.findViewById(R.id.txtWeb1);
//            holder.txtPh2 = view.findViewById(R.id.txtPh2);
//            holder.txtAddress2 = view.findViewById(R.id.txtAddress2);
//            holder.txtEmail2 = view.findViewById(R.id.txtEmail2);
//            holder.txtWeb2 = view.findViewById(R.id.txtWeb2);
//            holder.txtName = view.findViewById(R.id.textView);
//            holder.txtName1 = view.findViewById(R.id.textView5);
//            holder.txtName2 = view.findViewById(R.id.textView1);
//            holder.txtPos = view.findViewById(R.id.textView6);
//            holder.txtPos1 = view.findViewById(R.id.textView12);
//            holder.txtPos2 = view.findViewById(R.id.textView2);
//
//            /*
//
//                views
//
//             */
//            holder.halfView = view.findViewById(R.id.halfView);
//            holder.semiView = view.findViewById(R.id.semiView);
//            holder.rightView = view.findViewById(R.id.rightView);
//
//            return view;
//        }
//
//        @Override
//        public void bindView(View view, Context context, Cursor cursor) {
//            super.bindView(view, context, cursor);
//
//            final ViewHolder holder = (ViewHolder) view.getTag();
//
//            String name = cursor.getString(cursor.getColumnIndex(DbHandler.user_name));
//            String email = cursor.getString(cursor.getColumnIndex(DbHandler.user_email));
//            String phone = cursor.getString(cursor.getColumnIndex(DbHandler.user_phone));
//            String web = cursor.getString(cursor.getColumnIndex(DbHandler.user_website));
//            String address = cursor.getString(cursor.getColumnIndex(DbHandler.user_address));
//            String position = cursor.getString(cursor.getColumnIndex(DbHandler.user_position));
//            String device_id = cursor.getString(cursor.getColumnIndex(DbHandler.user_device_id));
//            String user_logo = cursor.getString(cursor.getColumnIndex(DbHandler.user_logo));
//            String colorCode = cursor.getString(cursor.getColumnIndex(DbHandler.color_code));
//            String used_layout = cursor.getString(cursor.getColumnIndex(DbHandler.used_layout));
//
//            if (used_layout.equals("1")){
//                holder.rightView.setVisibility(View.VISIBLE);
//            }
//
//            if (used_layout.equals("2")){
//                holder.semiView.setVisibility(View.VISIBLE);
//            }
//
//            if (used_layout.equals("3")){
//                holder.halfView.setVisibility(View.VISIBLE);
//            }
//
//
//            GradientDrawable drawable = (GradientDrawable) holder.phImg.getBackground();
//            drawable.setColor(Integer.parseInt(colorCode));
//
//            GradientDrawable drawable1 = (GradientDrawable) holder.webImg.getBackground();
//            drawable1.setColor(Integer.parseInt(colorCode));
//
//            GradientDrawable drawable2 = (GradientDrawable) holder.emailImg.getBackground();
//            drawable2.setColor(Integer.parseInt(colorCode));
//
//            GradientDrawable drawable3 = (GradientDrawable) holder.locImg.getBackground();
//            drawable3.setColor(Integer.parseInt(colorCode));
//
//            GradientDrawable drawable4 = (GradientDrawable) holder.phImg1.getBackground();
//            drawable4.setColor(Integer.parseInt(colorCode));
//
//            GradientDrawable drawable5 = (GradientDrawable) holder.webImg1.getBackground();
//            drawable5.setColor(Integer.parseInt(colorCode));
//
//            GradientDrawable drawable6 = (GradientDrawable) holder.emailImg1.getBackground();
//            drawable6.setColor(Integer.parseInt(colorCode));
//
//            GradientDrawable drawable7 = (GradientDrawable) holder.locImg1.getBackground();
//            drawable7.setColor(Integer.parseInt(colorCode));
//
//            GradientDrawable drawable8 = (GradientDrawable) holder.phImg2.getBackground();
//            drawable8.setColor(Integer.parseInt(colorCode));
//
//            GradientDrawable drawable9 = (GradientDrawable) holder.webImg2.getBackground();
//            drawable9.setColor(Integer.parseInt(colorCode));
//
//            GradientDrawable drawable10 = (GradientDrawable) holder.emailImg2.getBackground();
//            drawable10.setColor(Integer.parseInt(colorCode));
//
//            GradientDrawable drawable11 = (GradientDrawable) holder.locImg2.getBackground();
//            drawable11.setColor(Integer.parseInt(colorCode));
//
//            holder.txtName.setText(name);
//            holder.txtEmail.setText(email);
//            holder.txtWeb.setText(web);
//            holder.txtAddress.setText(address);
//            holder.txtPos.setText(position);
//            holder.txtPh.setText(phone);
//
//            holder.txtName1.setText(name);
//            holder.txtEmail1.setText(email);
//            holder.txtWeb1.setText(web);
//            holder.txtAddress1.setText(address);
//            holder.txtPos1.setText(position);
//            holder.txtPh1.setText(phone);
//
//            holder.txtName2.setText(name);
//            holder.txtEmail2.setText(email);
//            holder.txtWeb2.setText(web);
//            holder.txtAddress2.setText(address);
//            holder.txtPos2.setText(position);
//            holder.txtPh2.setText(phone);
//
//            holder.txtPh.setLinkTextColor(Integer.parseInt(colorCode));
//            holder.txtWeb.setLinkTextColor(Integer.parseInt(colorCode));
//            holder.txtEmail.setLinkTextColor(Integer.parseInt(colorCode));
//            holder.txtAddress.setTextColor(Integer.parseInt(colorCode));
//            holder.txtPh1.setLinkTextColor(Integer.parseInt(colorCode));
//            holder.txtWeb1.setLinkTextColor(Integer.parseInt(colorCode));
//            holder.txtEmail1.setLinkTextColor(Integer.parseInt(colorCode));
//            holder.txtAddress1.setTextColor(Integer.parseInt(colorCode));
//            holder.txtPh2.setLinkTextColor(Integer.parseInt(colorCode));
//            holder.txtWeb2.setLinkTextColor(Integer.parseInt(colorCode));
//            holder.txtEmail2.setLinkTextColor(Integer.parseInt(colorCode));
//            holder.txtAddress2.setTextColor(Integer.parseInt(colorCode));
//
//            File pictureFileDir1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ".ezivizi");
//            String filename1 = pictureFileDir1.getPath() +File.separator+ Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)+".jpg";
//
//            holder.halfView.setCardBackgroundColor(Integer.parseInt(colorCode));
//            holder.semiView.setCardBackgroundColor(Integer.parseInt(colorCode));
//            holder.rightView.setCardBackgroundColor(Integer.parseInt(colorCode));
//
//            if (user_logo.equals(filename1)){
//                System.out.println("file : "+filename1);
//                Glide.with(getActivity()).load(user_logo).into(holder.img);
//                Glide.with(getActivity()).load(user_logo).into(holder.img1);
//                Glide.with(getActivity()).load(user_logo).into(holder.img2);
//            }else{
//                File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ".ezivizi");
//                String filename = pictureFileDir.getPath() +File.separator+user_logo;
//
//                Glide.with(getActivity()).load(filename).into(holder.img);
//                Glide.with(getActivity()).load(filename).into(holder.img1);
//                Glide.with(getActivity()).load(filename).into(holder.img2);
//            }
//
//
//
//        }
//
//        public class ViewHolder {
//            CardView halfView, rightView, semiView;
//            ImageView phImg, locImg, webImg, emailImg, phImg1, locImg1, webImg1, emailImg1, phImg2, locImg2, webImg2, emailImg2, img, img1, img2;
//            TextView txtPh, txtEmail, txtWeb, txtAddress, txtPh1, txtEmail1, txtWeb1, txtAddress1, txtPh2, txtEmail2, txtWeb2, txtAddress2, txtName, txtName1, txtName2, txtPos, txtPos1, txtPos2;
//
//        }
//    }

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
                adapterClass.getFilter().filter(newText);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                adapterClass.getFilter().filter(query);
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
                //openSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
