//	Copyright 2009-2010, by the California Institute of Technology.
//	ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
//	Any commercial use must be negotiated with the Office of Technology 
//	Transfer at the California Institute of Technology.
//	
//	This software is subject to U. S. export control laws and regulations 
//	(22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software 
//	is subject to U.S. export control laws and regulations, the recipient has 
//	the responsibility to obtain export licenses or other export authority as 
//	may be required before exporting such information to foreign countries or 
//	providing access to foreign nationals.
//	
//	$Id$
//

package gov.nasa.pds.registry.service;

import gov.nasa.pds.registry.model.Association;
import gov.nasa.pds.registry.model.AuditableEvent;
import gov.nasa.pds.registry.model.EventType;
import gov.nasa.pds.registry.model.ObjectStatus;
import gov.nasa.pds.registry.model.PagedResponse;
import gov.nasa.pds.registry.model.Product;
import gov.nasa.pds.registry.model.StatusInfo;
import gov.nasa.pds.registry.model.naming.IdentifierGenerator;
import gov.nasa.pds.registry.model.naming.Versioner;
import gov.nasa.pds.registry.query.AssociationQuery;
import gov.nasa.pds.registry.query.ProductQuery;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("registryService")
public class RegistryService {

	@Autowired
	MetadataStore metadataStore;

	@Autowired
	@Qualifier("versioner")
	Versioner versioner;

	@Autowired
	@Qualifier("idGenerator")
	IdentifierGenerator idGenerator;

	@Autowired
	@Qualifier("statusInfo")
	StatusInfo statusInfo;

	public void setMetadataStore(MetadataStore metadataStore) {
		this.metadataStore = metadataStore;
	}

	public MetadataStore getMetadataStore() {
		return metadataStore;
	}

	public void setVersioner(Versioner versioner) {
		this.versioner = versioner;
	}

	public Versioner getVersioner() {
		return this.versioner;
	}

	public void setIdentifierGenerator(IdentifierGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public IdentifierGenerator getIdentifierGenerator() {
		return this.idGenerator;
	}

	public PagedResponse getProducts(Integer start, Integer rows) {
		PagedResponse page = new PagedResponse(start, metadataStore
				.getNumProducts());
		page.setResults(metadataStore.getProducts(start, rows));
		return page;
	}

	public PagedResponse getProducts(ProductQuery query) {
		return this.getProducts(query, 1, 20);
	}

	public PagedResponse getProducts(ProductQuery query, Integer start,
			Integer rows) {
		return metadataStore.getProducts(query, start, rows);
	}

	public StatusInfo getStatus() {
		return this.statusInfo;
	}

	public Product publishProduct(String user, Product product) {
		// Check to see if there is an existing product with the same lid and
		// userVersion
		// TODO: Make this throw some exception instead
		if (metadataStore
				.hasProduct(product.getLid(), product.getVersionId())) {
			return null;
		}
		product.setGuid(idGenerator.getGuid());
		product.setHome(idGenerator.getHome());
		product.setVersionName(versioner.getInitialVersion());
		product.setStatus(ObjectStatus.Submitted);
		metadataStore.saveProduct(product);
		AuditableEvent event = new AuditableEvent(EventType.CREATED, product
				.getGuid(), user);
		event.setGuid(idGenerator.getGuid());
		event.setHome(idGenerator.getHome());
		metadataStore.saveAuditableEvent(event);
		return product;
	}

	public Product versionProduct(String user, String lid, Product product,
			boolean major) {
		Product referencedProduct = this.getLatestProduct(lid);
		product.setGuid(idGenerator.getGuid());
		product.setHome(idGenerator.getHome());
		product.setVersionName(versioner.getNextVersion(referencedProduct
				.getVersionName(), major));
		product.setStatus(referencedProduct.getStatus());
		metadataStore.saveProduct(product);
		AuditableEvent event = new AuditableEvent(EventType.VERSIONED,
				referencedProduct.getGuid(), user);
		event.setGuid(idGenerator.getGuid());
		event.setHome(idGenerator.getHome());
		metadataStore.saveAuditableEvent(event);
		return metadataStore.getProduct(product.getGuid());
	}

	public Product getLatestProduct(String lid) {
		List<Product> products = metadataStore.getProductVersions(lid);
		Collections.sort(products, versioner.getComparator());
		if (products.size() > 0) {
			return products.get(products.size() - 1);
		}
		return null;
	}

	public Product getEarliestProduct(String lid) {
		List<Product> products = metadataStore.getProductVersions(lid);
		Collections.sort(products, versioner.getComparator());
		if (products.size() > 0) {
			return products.get(0);
		}
		return null;
	}

	// TODO: Make this method throw an exception if the lid or version is not
	// found
	public Product getNextProduct(String lid, String versionId) {
		List<Product> products = metadataStore.getProductVersions(lid);
		Collections.sort(products, versioner.getComparator());
		for (int i = 0; i < products.size(); i++) {
			Product product = products.get(i);
			if (versionId.equals(product.getVersionId())) {
				if (i < products.size() - 1) {
					return products.get(i + 1);
				} else {
					return null;
				}
			}
		}
		return null;
	}

	// TODO: Make this method throw an exception if the lid or version is not
	// found
	public Product getPreviousProduct(String lid, String versionId) {
		List<Product> products = metadataStore.getProductVersions(lid);
		Collections.sort(products, versioner.getComparator());
		for (int i = 0; i < products.size(); i++) {
			Product product = products.get(i);
			if (versionId.equals(product.getVersionId())) {
				if (i > 0) {
					return products.get(i - 1);
				} else {
					return null;
				}
			}
		}
		return null;
	}

	public List<Product> getProductVersions(String lid) {
		return metadataStore.getProductVersions(lid);
	}

	public Product getProduct(String lid, String versionId) {
		return metadataStore.getProduct(lid, versionId);
	}

	public Product changeStatus(String lid, String versionId,
			ObjectStatus status) {
		Product product = metadataStore.getProduct(lid, versionId);
		product.setStatus(status);
		metadataStore.updateProduct(product);
		return product;
	}

	public void deleteProduct(String user, String lid, String versionId) {
		Product product = metadataStore.getProduct(lid, versionId);
		if (product != null) {
			metadataStore.deleteProduct(lid, versionId);
			AuditableEvent event = new AuditableEvent(EventType.DELETED,
					product.getGuid(), user);
	    event.setGuid(idGenerator.getGuid());
	    event.setHome(idGenerator.getHome());
			metadataStore.saveAuditableEvent(event);
		}
	}

	public Product updateProduct(Product product) {
		return null;
	}

	public PagedResponse getAssociations(AssociationQuery query, Integer start,
			Integer rows) {
		return metadataStore.getAssociations(query, start, rows);
	}

	public PagedResponse getAssociations(String lid, String versionId,
			Integer start, Integer rows) {
		return metadataStore.getAssociations(lid, versionId, start, rows);
	}

	public Association publishAssociation(String user, Association association) {
		association.setGuid(idGenerator.getGuid());
		association.setHome(idGenerator.getHome());
		if (association.getSourceHome() == null) {
			association.setSourceHome(idGenerator.getHome());
		}
		if (association.getTargetHome() == null) {
			association.setTargetHome(idGenerator.getHome());
		}
		association.setLid(association.getGuid());
		association.setVersionName(versioner.getInitialVersion());
		association.setStatus(ObjectStatus.Submitted);
		metadataStore.saveAssociation(association);
		AuditableEvent event = new AuditableEvent(EventType.CREATED,
				association.getGuid(), user);
		event.setGuid(idGenerator.getGuid());
		event.setHome(idGenerator.getHome());
		metadataStore.saveAuditableEvent(event);
		return association;
	}

	public Association getAssocation(String guid) {
		return metadataStore.getAssociation(guid);
	}
	
	public void deleteAssociation(String user, String guid) {
	  Association association = metadataStore.getAssociation(guid);
	  if (association != null) {
	    metadataStore.deleteAssociation(guid);
      AuditableEvent event = new AuditableEvent(EventType.DELETED,
          association.getGuid(), user);
      event.setGuid(idGenerator.getGuid());
      event.setHome(idGenerator.getHome());
      metadataStore.saveAuditableEvent(event);
	  }
	}
}
