/*
 * SpoonacularAPILib
 *
 * This file was automatically generated by APIMATIC v2.0 ( https://apimatic.io ).
 */
package cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.controllers;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.http.protocol.HTTP;

import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.*;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.models.*;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.exceptions.*;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.http.client.HttpClient;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.http.client.HttpContext;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.http.request.HttpRequest;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.http.response.HttpResponse;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.http.response.HttpStringResponse;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.http.client.APICallBack;

public class APIController extends BaseController {
    //private static variables for the singleton pattern
    private static Object syncObject = new Object();
    private static APIController instance = null;

    /**
     * Singleton pattern implementation
     * @return The singleton instance of the APIController class
     */
    public static APIController getInstance() {
        synchronized (syncObject) {
            if (null == instance) {
                instance = new APIController();
            }
        }
        return instance;
    }

    /**
     * Get information about a recipe.
     * @param    id    Required parameter: The id of the recipe.
     * @return    Returns the void response from the API call
     */
    public void getRecipeInformationAsync(
            final int id,
            final APICallBack<DynamicResponse> callBack
    ) {
        //the base uri for api requests
        String _baseUri = Configuration.baseUri;

        //prepare query string for API call
        StringBuilder _queryBuilder = new StringBuilder(_baseUri);
        _queryBuilder.append("/recipes/{id}/information");

        //process template parameters
        APIHelper.appendUrlWithTemplateParameters(_queryBuilder, new HashMap<String, Object>() {
            private static final long serialVersionUID = 5755009982553801133L;
            {
                put( "id", id );
            }});
        //validate and preprocess url
        String _queryUrl = APIHelper.cleanUrl(_queryBuilder);

        //load all headers for the outgoing API request
        Map<String, String> _headers = new HashMap<String, String>() {
            private static final long serialVersionUID = 5178132500079624150L;
            {
                put( "user-agent", "APIMATIC 2.0" );
                put( "accept", "application/json" );
                put( "X-Mashape-Key", Configuration.getXMashapeKey() );
            }
        };

        //prepare and invoke the API call request to fetch the response
        final HttpRequest _request = getClientInstance().get(_queryUrl, _headers, null);

        //invoke the callback before request if its not null
        if (getHttpCallBack() != null)
        {
            getHttpCallBack().OnBeforeRequest(_request);
        }

        //invoke request and get response
        Runnable _responseTask = new Runnable() {
            public void run() {
                //make the API call

                APICallBack<HttpResponse> x = new APICallBack<HttpResponse>() {
                    public void onSuccess(HttpContext _context, HttpResponse _response) {
                        try {

                            //invoke the callback after response if its not null
                            if (getHttpCallBack() != null)
                            {
                                getHttpCallBack().OnAfterResponse(_context);
                            }

                            //handle errors defined at the API level
                            validateResponse(_response, _context);

                            //extract result from the http response
                            DynamicResponse _result = new DynamicResponse(_response);

                            callBack.onSuccess(_context, _response);
                        } catch (APIException error) {
                            //let the caller know of the error
                            callBack.onFailure(_context, error);
                        } catch (Exception exception) {
                            //let the caller know of the caught Exception
                            callBack.onFailure(_context, exception);
                        }
                    }
                    public void onFailure(HttpContext _context, Throwable _error) {
                        //invoke the callback after response if its not null
                        if (getHttpCallBack() != null)
                        {
                            getHttpCallBack().OnAfterResponse(_context);
                        }

                        //let the caller know of the failure
                        callBack.onFailure(_context, _error);
                    }
                };

                getClientInstance().executeAsStringAsync(_request, x);
            }
        };

        //execute async using thread pool
        APIHelper.getScheduler().execute(_responseTask);
    }

    /**
     * Find recipes that use as many of the given ingredients as possible and have as little as possible missing ingredients. This is a whats in your fridge API endpoint.
     * @param    ingredients    Required parameter: A comma-separated list of ingredients that the recipes should contain.
     * @param    limitLicense    Optional parameter: Whether to only show recipes with an attribution license.
     * @param    number    Optional parameter: The maximal number of recipes to return (default = 5).
     * @param    ranking    Optional parameter: Whether to maximize used ingredients (1) or minimize missing ingredients (2) first
     * @return    Returns the void response from the API call
     */
    public void findByIngredientsAsync(
            final String ingredients,
            final Boolean limitLicense,
            final Integer number,
            final Integer ranking,
            final APICallBack<List<FindByIngredientsModel>> callBack
    ) {
        //the base uri for api requests
        String _baseUri = Configuration.baseUri;

        //prepare query string for API call
        StringBuilder _queryBuilder = new StringBuilder(_baseUri);
        _queryBuilder.append("/recipes/findByIngredients");

        //process query parameters
        APIHelper.appendUrlWithQueryParameters(_queryBuilder, new HashMap<String, Object>() {
            private static final long serialVersionUID = 5452056102902388732L;
            {
                put("fillIngredients", false);
                put( "ingredients", ingredients );
                put( "limitLicense", (null != limitLicense) ? limitLicense : false );
                put( "number", (null != number) ? number : 5 );
                put( "ranking", (null != ranking) ? ranking : 1 );
            }});


        //deleted parameter cause I didn't use it . . .
        //append optional parameters to the query
        //APIHelper.appendUrlWithQueryParameters(_queryBuilder, queryParameters);
        //validate and preprocess url
        String _queryUrl = APIHelper.cleanUrl(_queryBuilder);

        //load all headers for the outgoing API request
        Map<String, String> _headers = new HashMap<String, String>() {
            private static final long serialVersionUID = 5724080638588955692L;
            {
               // put( "user-agent", "APIMATIC 2.0" );
                put( "Accept", "application/json" );
                put( "X-Mashape-Key", Configuration.getXMashapeKey() );
            }
        };

        //prepare and invoke the API call request to fetch the response
        final HttpRequest _request = getClientInstance().get(_queryUrl, _headers, null);

        //invoke the callback before request if its not null
        if (getHttpCallBack() != null)
        {
            getHttpCallBack().OnBeforeRequest(_request);
        }

        //invoke request and get response
        Runnable _responseTask = new Runnable() {
            public void run() {
                //make the API call
                APICallBack<HttpResponse> y = new APICallBack<HttpResponse>(){

                    public void onSuccess(HttpContext _context, HttpResponse _response) {
                        try {

                            //invoke the callback after response if its not null
                            if (getHttpCallBack() != null)
                            {
                                getHttpCallBack().OnAfterResponse(_context);
                            }

                            //handle errors defined at the API level
                            validateResponse(_response, _context);

                            //extract result from the http response
                            String _responseBody = ((HttpStringResponse)_response).getBody();
                            List<FindByIngredientsModel> _result = APIHelper.deserialize(_responseBody,
                                    new TypeReference<List<FindByIngredientsModel>>(){});

                            //let the caller know of the success
                            callBack.onSuccess(_context, _response);
                        } catch (APIException error) {
                            //let the caller know of the error
                            callBack.onFailure(_context, error);
                        } catch (IOException ioException) {
                            //let the caller know of the caught IO Exception
                            callBack.onFailure(_context, ioException);
                        } catch (Exception exception) {
                            //let the caller know of the caught Exception
                            callBack.onFailure(_context, exception);
                        }
                    }
                    public void onFailure(HttpContext _context, Throwable _error) {
                        //invoke the callback after response if its not null
                        if (getHttpCallBack() != null)
                        {
                            getHttpCallBack().OnAfterResponse(_context);
                        }

                        //let the caller know of the failure
                        callBack.onFailure(_context, _error);
                    }
                };

                getClientInstance().executeAsStringAsync(_request, y);
            }
        };

        //execute async using thread pool
        APIHelper.getScheduler().execute(_responseTask);
    }


    public void randomness(StringBuilder queryBuilder,
                           final APICallBack<String> callBack)
    {
        Map<String, String> _headers = new HashMap<String, String>() {
            private static final long serialVersionUID = 5724080638588955692L;
            {
                put( "accept", "application/json" );
                put( "X-Mashape-Key", Configuration.getXMashapeKey() );
            }
        };

        final HttpRequest _request = getClientInstance().get(APIHelper.cleanUrl(queryBuilder), _headers, null);

        //invoke the callback before request if its not null
        if (getHttpCallBack() != null)
        {
            getHttpCallBack().OnBeforeRequest(_request);
        }

        //invoke request and get response
        Runnable _responseTask = new Runnable() {
            public void run() {
                //make the API call
                getClientInstance().executeAsStringAsync(_request, new APICallBack<HttpResponse>() {
                    public void onSuccess(HttpContext _context, HttpResponse _response) {
                        try {

                            //invoke the callback after response if its not null
                            if (getHttpCallBack() != null)
                            {
                                getHttpCallBack().OnAfterResponse(_context);
                            }

                            //handle errors defined at the API level
                            validateResponse(_response, _context);

                            //extract result from the http response
                            String _responseBody = ((HttpStringResponse)_response).getBody();
                            DynamicResponse _result = new DynamicResponse(_response);
                            //let the caller know of the success
                            callBack.onSuccess(_context, _response);
                        } catch (APIException error) {
                            //let the caller know of the error
                            callBack.onFailure(_context, error);
                        } catch (Exception exception) {
                            //let the caller know of the caught Exception
                            callBack.onFailure(_context, exception);
                        }
                    }
                    public void onFailure(HttpContext _context, Throwable _error) {
                        //invoke the callback after response if its not null
                        if (getHttpCallBack() != null)
                        {
                            getHttpCallBack().OnAfterResponse(_context);
                        }

                        //let the caller know of the failure
                        callBack.onFailure(_context, _error);
                    }
                });
            }
        };

        //execute async using thread pool
        APIHelper.getScheduler().execute(_responseTask);


    }
}