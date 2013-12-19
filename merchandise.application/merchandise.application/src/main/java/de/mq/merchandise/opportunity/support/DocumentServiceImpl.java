package de.mq.merchandise.opportunity.support;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import de.mq.merchandise.util.BasicServiceImpl;


@Service
public class DocumentServiceImpl extends BasicServiceImpl<DocumentsAware> implements DocumentService  {
	
	private final DocumentRepository documentRepository;

	private final DocumentEntityRepository documentEntityRepository;
	
	
	
	
	@Autowired
	public DocumentServiceImpl(final DocumentRepository documentRepository, final DocumentEntityRepository documentEntityRepository) {
		super(documentEntityRepository);
		this.documentRepository = documentRepository;
		this.documentEntityRepository = documentEntityRepository;
	}
	

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public final  void upload(DocumentsAware documentAware,  final String name , final InputStream inputStream, final String contentType ) {
		
		final DocumentsAware document = documentEntityRepository.forId(documentAware.id(), documentAware.getClass());
	
		documentRepository.assign(document, name, inputStream, MediaType.parseMediaType(contentType));
		
		document.assignDocument(name);
		
		documentEntityRepository.save(document);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public final  void assignLink(DocumentsAware documentAware,  final String name) {
		final DocumentsAware document = documentEntityRepository.forId(documentAware.id(), documentAware.getClass());
		document.assignWebLink(name);
		documentEntityRepository.save(document);
	}


	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public final void delete(final DocumentsAware documentAware,  final String name ) {
		final DocumentsAware document = documentEntityRepository.forId(documentAware.id(), documentAware.getClass());
		documentRepository.delete(document, name);
		document.removeDocument(name);
		documentEntityRepository.save(document);
	}

	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public final byte[] document(final Long id, String name){
		return documentRepository.document(documentEntityRepository.forId(id), name);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public final byte[] document(final Long id){
		final DocumentsAware documentsAware = documentEntityRepository.forId(id);
		documentAwareGuard(documentsAware);
		return documentRepository.document(documentEntityRepository.forId(id), documentsAware.documents().keySet().iterator().next());
	}


	private void documentAwareGuard(final DocumentsAware documentsAware) {
		if( documentsAware.documents().size() != 1 ){
			throw new ResourceAccessException("One and only one Document must be aware");
		}
	}
	
	

}
