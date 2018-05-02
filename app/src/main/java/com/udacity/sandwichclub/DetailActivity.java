package com.udacity.sandwichclub;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.databinding.ActivityDetailBinding;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private static final String DELIMITER = ", ";
    private static final String DEFAULT_ALIASES = "No other names known";
    private static final String DEFAULT_INGREDIENTS = "Ingredients not available";
    private static final String DEFAULT_ORIGIN = "Not known";

    ActivityDetailBinding detailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_detail);
        detailBinding = DataBindingUtil.setContentView(this , R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String jsonText = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(jsonText);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        // moved all UI fields into one method
        populateUI(sandwich);

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Populate activity views with sandwich details
     * @param sandwich Sandwich object with details for the activity
     */
    private void populateUI(Sandwich sandwich) {
        // Get string values and set defaults if necessary
        String mainNameValue = sandwich.getMainName();
        String imgUrlValue = sandwich.getImage();
        String originValue = sandwich.getPlaceOfOrigin();
        if(originValue == null || originValue.trim().equals("")) {
            originValue = DEFAULT_ORIGIN;
        }
        String knownAsValue = sandwich.getAlsoKnownAs().size()==0 ?
                DEFAULT_ALIASES : TextUtils.join(DELIMITER, sandwich.getAlsoKnownAs());
        String ingredientsValue = sandwich.getIngredients().size()==0 ?
                DEFAULT_INGREDIENTS: TextUtils.join(DELIMITER, sandwich.getIngredients());
        String descriptionValue = sandwich.getDescription();

        /*
        ImageView ingredientsIv = findViewById(R.id.image_iv);
        TextView mainNameTv = findViewById(R.id.main_name_tv);
        TextView alsoKnownAsTv = findViewById(R.id.also_known_tv);
        TextView originTv = findViewById(R.id.origin_tv);
        TextView descriptionTv = findViewById(R.id.description_tv);
        TextView ingredientsTv = findViewById(R.id.ingredients_tv);
        */

        setTitle(mainNameValue);

        Picasso.with(this)
                .load(imgUrlValue)
                .error(R.mipmap.no_image_available) // using an image with text (created in Pinta)
                .into(detailBinding.imageIv);

        detailBinding.mainNameTv.setText(mainNameValue);
        detailBinding.sandwichInfo.alsoKnownTv.setText(knownAsValue);
        detailBinding.sandwichInfo.originTv.setText(originValue);
        detailBinding.sandwichInfo.descriptionTv.setText(descriptionValue);
        detailBinding.sandwichInfo.ingredientsTv.setText(ingredientsValue);

    }
}
