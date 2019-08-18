package com.kandktech.ezivizi.corporate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.kandktech.ezivizi.DbHandler;
import com.kandktech.ezivizi.QRGenerateActivity;
import com.kandktech.ezivizi.R;

import com.kandktech.ezivizi.model_class.SavedUserDetailModelClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class CorporateList extends AppCompatActivity {

    RecyclerView listView;

    DbHandler dbHandler;

    UserAdapterClass adapterClass;
    Cursor cursor = null;

    List<SavedUserDetailModelClass> userDetailModelClasses;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporate_list);

        listView = findViewById(R.id.recyclerView);

        dbHandler = new DbHandler(getApplicationContext());

        userDetailModelClasses = new ArrayList<>();

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
                    userDetail.setCompany(cursor.getString(cursor.getColumnIndex(DbHandler.company)));
                    userDetail.setFaxNo(cursor.getString(cursor.getColumnIndex(DbHandler.fax_no)));
                    userDetail.setPoBoxNo(cursor.getString(cursor.getColumnIndex(DbHandler.po_box_no)));


                    userDetailModelClasses.add(userDetail);

                } while (cursor.moveToNext());

            }

            adapterClass = new UserAdapterClass(getApplicationContext(),userDetailModelClasses);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            listView.setLayoutManager(mLayoutManager);
            listView.setItemAnimator(new DefaultItemAnimator());
            listView.setHasFixedSize(true);
            listView.setAdapter(adapterClass);
            cursor.requery();
            adapterClass.notifyDataSetChanged();

        }
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

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.corporate_list_item,viewGroup,false);

            return new UserAdapterClass.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserAdapterClass.MyViewHolder holder, int i) {

            SavedUserDetailModelClass savedUserDetailModelClass = savedUserDetailModelClassList.get(i);

            if (savedUserDetailModelClass.getUsed_layout().equals("1")){
                holder.rightView.setVisibility(View.GONE);
                holder.semiView.setVisibility(View.GONE);
                holder.curveView.setVisibility(View.GONE);
                holder.halfCurveView.setVisibility(View.GONE);
                holder.up_downView.setVisibility(View.GONE);
                holder.sideView.setVisibility(View.GONE);
                holder.halfView.setVisibility(View.VISIBLE);

            }
            if (savedUserDetailModelClass.getUsed_layout().equals("2")){
                holder.rightView.setVisibility(View.GONE);
                holder.semiView.setVisibility(View.GONE);
                holder.curveView.setVisibility(View.GONE);
                holder.halfCurveView.setVisibility(View.GONE);
                holder.up_downView.setVisibility(View.GONE);
                holder.sideView.setVisibility(View.VISIBLE);
                holder.halfView.setVisibility(View.GONE);
            }
            if (savedUserDetailModelClass.getUsed_layout().equals("3")){
                holder.rightView.setVisibility(View.GONE);
                holder.semiView.setVisibility(View.GONE);
                holder.curveView.setVisibility(View.VISIBLE);
                holder.halfCurveView.setVisibility(View.GONE);
                holder.up_downView.setVisibility(View.GONE);
                holder.sideView.setVisibility(View.GONE);
                holder.halfView.setVisibility(View.GONE);
            }

            if (savedUserDetailModelClass.getUsed_layout().equals("4")){
                holder.rightView.setVisibility(View.GONE);
                holder.semiView.setVisibility(View.GONE);
                holder.curveView.setVisibility(View.GONE);
                holder.halfCurveView.setVisibility(View.VISIBLE);
                holder.up_downView.setVisibility(View.GONE);
                holder.sideView.setVisibility(View.GONE);
                holder.halfView.setVisibility(View.GONE);
            }

            if (savedUserDetailModelClass.getUsed_layout().equals("5")){
                holder.rightView.setVisibility(View.GONE);
                holder.semiView.setVisibility(View.GONE);
                holder.curveView.setVisibility(View.GONE);
                holder.halfCurveView.setVisibility(View.GONE);
                holder.up_downView.setVisibility(View.VISIBLE);
                holder.sideView.setVisibility(View.GONE);
                holder.halfView.setVisibility(View.GONE);
            }

            if (savedUserDetailModelClass.getUsed_layout().equals("6")){
                holder.rightView.setVisibility(View.VISIBLE);
                holder.semiView.setVisibility(View.GONE);
                holder.curveView.setVisibility(View.GONE);
                holder.halfCurveView.setVisibility(View.GONE);
                holder.up_downView.setVisibility(View.GONE);
                holder.sideView.setVisibility(View.GONE);
                holder.halfView.setVisibility(View.GONE);
            }

            if (savedUserDetailModelClass.getUsed_layout().equals("7")){
                holder.rightView.setVisibility(View.GONE);
                holder.semiView.setVisibility(View.VISIBLE);
                holder.curveView.setVisibility(View.GONE);
                holder.halfCurveView.setVisibility(View.GONE);
                holder.up_downView.setVisibility(View.GONE);
                holder.sideView.setVisibility(View.GONE);
                holder.halfView.setVisibility(View.GONE);
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
                holder.txtFax1.setText(faxNo);
                holder.txtFax2.setText(faxNo);
                holder.txtFax3.setText(faxNo);
                holder.txtFax4.setText(faxNo);
                holder.txtFax5.setText(faxNo);
                holder.txtFax6.setText(faxNo);
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
                holder.txtPo1.setText(poBoxNo);
                holder.txtPo2.setText(poBoxNo);
                holder.txtPo3.setText(poBoxNo);
                holder.txtPo4.setText(poBoxNo);
                holder.txtPo5.setText(poBoxNo);
                holder.txtPo6.setText(poBoxNo);
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
            holder.txtPh3.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtWeb3.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtEmail3.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtAddress3.setTextColor(Integer.parseInt(colorCode));
            holder.txtPh4.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtWeb4.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtEmail4.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtAddress4.setTextColor(Integer.parseInt(colorCode));
            holder.txtPh5.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtWeb5.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtEmail5.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtAddress5.setTextColor(Integer.parseInt(colorCode));
            holder.txtPh6.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtWeb6.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtEmail6.setLinkTextColor(Integer.parseInt(colorCode));
            holder.txtAddress6.setTextColor(Integer.parseInt(colorCode));

            holder.halfView.setBackgroundColor(Integer.parseInt(colorCode));
            holder.semiView.setCardBackgroundColor(Integer.parseInt(colorCode));
            holder.rightView.setBackgroundColor(Integer.parseInt(colorCode));
            holder.sideView.setBackgroundColor(Integer.parseInt(colorCode));
            holder.curveView.setBackgroundColor(Integer.parseInt(colorCode));
            holder.halfCurveView.setBackgroundColor(Integer.parseInt(colorCode));
            holder.up_downView.setBackgroundColor(Integer.parseInt(colorCode));

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

            GradientDrawable drawable12 = (GradientDrawable) holder.phImg3.getBackground();
            drawable12.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable13 = (GradientDrawable) holder.webImg3.getBackground();
            drawable13.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable14 = (GradientDrawable) holder.emailImg3.getBackground();
            drawable14.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable15 = (GradientDrawable) holder.locImg3.getBackground();
            drawable15.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable16 = (GradientDrawable) holder.phImg4.getBackground();
            drawable16.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable17 = (GradientDrawable) holder.webImg4.getBackground();
            drawable17.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable18 = (GradientDrawable) holder.emailImg4.getBackground();
            drawable18.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable19 = (GradientDrawable) holder.locImg4.getBackground();
            drawable19.setColor(Integer.parseInt(colorCode));

            File pictureFileDir1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ".ezivizi");
            String filename1 = pictureFileDir1.getPath() +File.separator+ Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)+".jpg";


            Glide.with(getApplicationContext()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img);
            Glide.with(getApplicationContext()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img1);
            Glide.with(getApplicationContext()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img2);
            Glide.with(getApplicationContext()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img3);
            Glide.with(getApplicationContext()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img4);
            Glide.with(getApplicationContext()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img5);
            Glide.with(getApplicationContext()).load(savedUserDetailModelClass.getUser_logo()).into(holder.img6);





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
            CardView halfView, rightView;
            CardView semiView, curveView, halfCurveView, sideView, up_downView;
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
            TextView txtCompany1, txtCompany2, txtCompany3, txtCompany4, txtCompany5, txtCompany6;


            public MyViewHolder(@NonNull View view) {
                super(view);

                halfView = view.findViewById(R.id.halfView);
                semiView = view.findViewById(R.id.semiView);
                rightView = view.findViewById(R.id.rightView);
                curveView = view.findViewById(R.id.curveView);
                sideView = view.findViewById(R.id.sideView);
                halfCurveView = view.findViewById(R.id.halfCurveView);
                up_downView = view.findViewById(R.id.UpDownView);



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


}
