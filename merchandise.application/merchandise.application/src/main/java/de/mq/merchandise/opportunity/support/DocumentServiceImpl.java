package de.mq.merchandise.opportunity.support;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentServiceImpl implements DocumentService {
	
	private final DocumentRepository documentRepository;

	private final DocumentEntityRepository documentEntityRepository;
	
	
	private final Tika tika;
	
	@Autowired
	public DocumentServiceImpl(final DocumentRepository documentRepository, final DocumentEntityRepository documentEntityRepository, final Tika tika) {
		this.documentRepository = documentRepository;
		this.documentEntityRepository = documentEntityRepository;
		this.tika=tika;
	}
	

	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.DocumentService#upload(de.mq.merchandise.opportunity.support.DocumentsAware, java.lang.String, java.io.InputStream)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public final  void upload(DocumentsAware documentAware,  final String name , final InputStream inputStream) {
		
		final DocumentsAware document = documentEntityRepository.forId(documentAware.id(), documentAware.getClass());
		
		documentRepository.assign(document, name, inputStream, contentType(inputStream));
		
		document.assignDocument(name);
		
		documentEntityRepository.save(document);
	}


	private MediaType contentType(final InputStream inputStream)  {
		
		try {
			return MediaType.parseMediaType(tika.detect(new BufferedInputStream(inputStream)));
		} catch (final IOException ex) {
			 throw new IllegalStateException(ex);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.DocumentService#delete(de.mq.merchandise.opportunity.support.DocumentsAware, java.lang.String)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public final void delete(final DocumentsAware documentAware,  final String name ) {
		final DocumentsAware document = documentEntityRepository.forId(documentAware.id(), documentAware.getClass());
		documentRepository.delete(documentAware, name);
		document.removeDocument(name);
		documentEntityRepository.save(documentAware);
	}
	
	

}
