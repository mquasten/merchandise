package de.mq.merchandise.opportunity.support;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentServiceImpl {
	
	private final DocumentRepository documentRepository;

	private final DocumentEntityRepository documentEntityRepository;
	
	public DocumentServiceImpl(final DocumentRepository documentRepository, final DocumentEntityRepository documentEntityRepository) {
		this.documentRepository = documentRepository;
		this.documentEntityRepository = documentEntityRepository;
	}
	

	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public final  void upload(DocumentsAware documentAware,  final String name , final InputStream inputStream) {
		
		final DocumentsAware document = documentEntityRepository.forId(documentAware.id(), documentAware.getClass());
		
		documentRepository.assign(document, name, inputStream, contentType(inputStream));
		
		document.assignDocument(name);
		
		documentEntityRepository.save(document);
	}


	private MediaType contentType(final InputStream inputStream)  {
		final Tika tika = new Tika();
		
		
		final InputStream is = new BufferedInputStream(inputStream);
		try {
			return MediaType.parseMediaType(tika.detect(is));
		} catch (final IOException ex) {
			 throw new IllegalStateException(ex);
		}
	}

}
