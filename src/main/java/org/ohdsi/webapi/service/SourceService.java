package org.ohdsi.webapi.service;

import java.util.ArrayList;
import java.util.Collection;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.ohdsi.webapi.source.Source;
import org.ohdsi.webapi.source.SourceDaimon;
import org.ohdsi.webapi.source.SourceInfo;
import org.ohdsi.webapi.source.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/source/")
@Component
public class SourceService extends AbstractDaoService {

  @Autowired
  private SourceRepository sourceRepository;

  @Path("sources")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Collection<SourceInfo> getSources() {
    ArrayList<SourceInfo> sources = new ArrayList<>();
    for (Source source : sourceRepository.findAll()) {
      sources.add(new SourceInfo(source));
    }
    return sources;
  }

  @Path("priorityVocabulary")
  @GET
  @Produces(MediaType.APPLICATION_JSON)  
  public SourceInfo getPriorityVocabularySourceInfo() {
    int priority = 0;
    SourceInfo priorityVocabularySourceInfo = null;

    for (Source source : sourceRepository.findAll()) {
      for (SourceDaimon daimon : source.getDaimons()) {
        if (daimon.getDaimonType() == SourceDaimon.DaimonType.Vocabulary) {
          int daimonPriority = Integer.parseInt(daimon.getPriority());
          if (daimonPriority >= priority) {
            priority = daimonPriority;
            priorityVocabularySourceInfo = new SourceInfo(source);
          }
        }
      }
    }
    
    return priorityVocabularySourceInfo;
  }

  @Path("{key}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public SourceInfo getSource(@PathParam("key") final String sourceKey) {
    return sourceRepository.findBySourceKey(sourceKey).getSourceInfo();
  }
}
