package com.abc.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dtos.ContactDto;
import com.abc.entities.ContactEntity;
import com.abc.exceptions.EntityNotFoundException;
import com.abc.repositories.ContactRepository;
import com.abc.utils.ContactMapper;

@Service
public class ContactService {

	@Autowired
	private ValidationService validationService;
	@Autowired
	private ContactRepository contactRepository;
	

	public void deleteEntity(Long id) {
		contactRepository.deleteById(id);
	}

	public Set<ContactDto> getAllEntities() {

		List<ContactEntity> contactDtos = contactRepository.findAll();		
		if (contactDtos == null) {
			throw new EntityNotFoundException();
		}
		return contactDtos
				.stream()
				.map(d -> ContactMapper.mapContactFromEntity(d))
				.collect(Collectors.toSet());		

	}
	

	public ContactDto getEntity(Long id) {
		Optional<ContactEntity> resp = contactRepository.findById(id);
		if(!resp.isPresent()) {
			throw new EntityNotFoundException();			
		}
		return ContactMapper.mapContactFromEntity(resp.get());

	}

	public ContactDto saveContact(ContactDto contactDetails) {
		validationService.validatePhone(contactDetails);
		ContactEntity contact = ContactMapper.mapContactToEntity(contactDetails);
		return ContactMapper.mapContactFromEntity(contactRepository.save(contact));

	}
	
	public ContactDto updateContact(Long id, ContactDto contactDetails) {
		validationService.validatePhone(contactDetails);
		ContactDto resp = getEntity(id);
		validationService.update(contactDetails, resp);
		ContactEntity contactEntity = ContactMapper.mapContactToEntity(resp);
		return ContactMapper.mapContactFromEntity(contactRepository.save(contactEntity));

	}
}
