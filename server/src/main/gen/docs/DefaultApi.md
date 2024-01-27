# DefaultApi

All URIs are relative to *https://OnlineEducation*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**rootGet**](DefaultApi.md#rootGet) | **GET** / |  |


<a name="rootGet"></a>
# **rootGet**
> rootGet()





### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://OnlineEducation");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    try {
      apiInstance.rootGet();
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#rootGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **502** | Bad Gateway |  -  |

