package cs301rclass.myapplicationattempt4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.APIHelper;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.Configuration;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.Deserilaizer;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.controllers.APIController;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.http.client.APICallBack;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.http.client.HttpContext;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.http.response.HttpResponse;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.http.response.HttpStringResponse;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.models.DynamicResponse;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.models.FindByIngredientsModel;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.models.RecipeURL;


public class RecipeViewer extends AppCompatActivity {

    EditText Ingredients;
    private List<Recipe> recipeResponses = new ArrayList<Recipe>();
    Button Submit;
    Configuration config;
    APIController api;
    ListView list;
    ArrayAdapter<Recipe> adapter;
    int numberToFind = 10;
    TextView instructions;
    Deserilaizer deserilaizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        config = new Configuration();
        config.initialize(this.getApplicationContext());
        api = APIController.getInstance();

        Ingredients = (EditText) findViewById(R.id.Ingredients);
        Ingredients.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                instructions.setText("Enter in a list of ingredients you want to use (seperated by commas) and we'll find some creative ways to use those items.  \\n\\t\\t example: apples,flour,sugar");
                instructions.setTextColor(getResources().getColor(R.color.Black));
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        Submit = (Button) findViewById(R.id.Submit);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Ingredients.getText().toString().length() > 0)
                {
                    SpoonacularAPIAttempt("");
                    //dummy data for practicing without hitting the API
                    //recipeResponses.add(new Recipe(100, "Apple Fritters", "https://spoonacular.com/recipeImages/Apple-fritters-556470.jpg"));

                }
                else {
                    recipeResponses.clear();
                    recipeResponses.add(new Recipe(-1, "Please enter ingredients first!", ""));
                    populateListView();
                }

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });


        adapter = new RecipeViewer.MyListAdapter();
        list = (ListView) findViewById(R.id.RecipeList);
        list.setAdapter(adapter);
        registerClickCallback();

        String s = getIntent().getStringExtra("Ingredients");
        Ingredients.setText(s);
        if(s.length() > 0)
        {
            if(s.compareTo("empty") == 0)
            {
                recipeResponses.add(new Recipe(-1, "Please enter ingredients first!", ""));
                populateListView();
            }
            else
            {
                SpoonacularAPIAttempt(s);
            }
        }

        instructions = (TextView) findViewById(R.id.instructions);
        deserilaizer = new Deserilaizer();
    }


/*  Node JS code given as an example using unirest

unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillIngredients=false&ingredients=apples%2Cflour%2Csugar&limitLicense=false&number=5&ranking=1")
.header("X-Mashape-Key", "U4VdTrjVsvmshyAo8AXsHbO3HUZLp1yXH5RjsnUEJzhCL1oKPy")
.header("X-Mashape-Host", "spoonacular-recipe-food-nutrition-v1.p.mashape.com")
.end(function (result) {
  console.log(result.status, result.headers, result.body);
});
*/


    //using the spoonacular example given online
    public void SpoonacularAPIAttempt(String s) {
        recipeResponses.clear();
        //   final String ingredients, final Boolean limitLicense,  final Integer number,  final Integer ranking,  Map<String, Object> queryParameters,
        //   final APICallBack<List<FindByIngredientsModel>> callBack

        final APICallBack<List<FindByIngredientsModel>> callBack = new APICallBack<List<FindByIngredientsModel>>() {
            @Override
            public void onSuccess(HttpContext context, HttpResponse hr) {
                //Success...
                try {
                    String _responseBody = ((HttpStringResponse)hr).getBody();
                    List<FindByIngredientsModel> response = APIHelper.deserialize(_responseBody,
                            new TypeReference<List<FindByIngredientsModel>>(){});

                    if (recipeResponses.size() > 0 && response.size() > 0)
                        if (recipeResponses.get(0).getID() == -1)
                            recipeResponses.clear();

                    for (int i = 0; i < response.size(); i++)
                        recipeResponses.add((response.get(i)).Conversion());
                    populateListView();
                }
                catch(Exception e)
                {
                    if(recipeResponses.size() == 0)
                        recipeResponses.add(new Recipe(-1, "Error with the API call.  Try Again", ""));
                }
            }

            @Override
            public void onFailure(HttpContext context, Throwable error) {
                if(recipeResponses.size() == 0)
                    recipeResponses.add(new Recipe(-1, "Error with the API call.  Try Again", ""));
            }
        };

        if(s.isEmpty())
            s = Ingredients.getText().toString().trim();
        api.findByIngredientsAsync(s, false, numberToFind, 1, callBack);

        if (recipeResponses.size() != 0)
            populateListView();
        else
        {
            recipeResponses.add(new Recipe(-1, "No recipes found", ""));
            populateListView();
        }
    }

    private void populateListView() {
        adapter.notifyDataSetChanged();
    }

    private void registerClickCallback() {
        list = (ListView) findViewById(R.id.RecipeList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Recipe clicked = recipeResponses.get(position);

                if (clicked.getID() != -1)
                {
                    getWebAddress(clicked.getID());

                }
            }
        });
    }

    private void getWebAddress(int id)
    {

        final APICallBack<DynamicResponse> callBack = new APICallBack<DynamicResponse>() {
            @Override
            public void onSuccess(HttpContext context, HttpResponse response) {
                try {
                    String _responseBody = ((HttpStringResponse) response).getBody();

                    String url = deserilaizer.Deserialize(_responseBody);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
                catch(Exception e)
                {
                    instructions.setText("Error getting the url from the API");
                    instructions.setTextColor(getResources().getColor(R.color.ErrorCodeRed));
                }
            }
            @Override
            public void onFailure(HttpContext context, Throwable error) {
                if(recipeResponses.size() == 0)
                    recipeResponses.add(new Recipe(-1, "Error with the API call.  Try Again", ""));
            }
        };

        api.getRecipeInformationAsync(id, callBack);
    }

    private class MyListAdapter extends ArrayAdapter<Recipe> {
        public MyListAdapter() {
            super(RecipeViewer.this, R.layout.recipe_item_view, recipeResponses);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //make sure we have a view to work with(may be given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.recipe_item_view, parent, false);
            }
            //find the food

            Recipe current = recipeResponses.get(position);
            if(current.getID() != -1) {
                //fill the view
                ImageView imageView = (ImageView) itemView.findViewById(R.id.item_icon);
                try {
                    URL url = new URL(current.getImage());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    imageView.setImageBitmap(myBitmap);
                } catch (Exception e) {
                    //create bit image that has x over
                }
            }

            //name
            TextView nameText = (TextView) itemView.findViewById(R.id.item_txtName);
            nameText.setText(current.getName());

            return itemView;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}
        if(id == R.id.Return)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}