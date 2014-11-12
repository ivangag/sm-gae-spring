package org.symptomcheck.capstone.gcm;

import java.util.ArrayList;
import java.util.List;

public class GcmResponse {
  
    public String multicast_id;
  
    public String success;
  
    public String failure;
  
    public String canonical_ids;
  
    public  List<GcmReponseResult> results = new ArrayList<GcmReponseResult>();

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("multicast_id:").append(multicast_id).append("\n")
          .append("success:").append(success).append("\n")
          .append("failure:").append(failure).append("\n")
          .append("canonical_ids:").append(canonical_ids).append("\n")
          .append("results:").append("[").append("\n")
          ;
                
        for(GcmReponseResult result : results)
        {
            sb.append("message_id:").append(result.getMessage_id()).append("\n")
                .append("registration_id:").append(result.getRegistration_id() != null ? result.getRegistration_id() : "").append("\n")
                .append("error:").append(result.getError() != null ? result.getError() :  "").append("\n");
        }
        sb.append("]").append("\n");
        return sb.toString();
    }
}
