package com.abc.services;


import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.abc.dtos.ContactDto;
import com.abc.dtos.PhoneDto;
import com.abc.exceptions.ContactValidationException;
import com.abc.utils.ConstantUtils;

@Service
public class ValidationService {

	public void update(ContactDto source, ContactDto dest) {

		if (!StringUtils.isEmpty(source.getEmail())) {
			dest.setEmail(source.getEmail());
		}

		if (source.getAddress() != null) {
			if (dest.getAddress() == null) {
				dest.setAddress(source.getAddress());
			} else {
				if (!StringUtils.isEmpty(source.getAddress().getStreet())) {
					dest.getAddress().setStreet(source.getAddress().getStreet());
				}
				if (!StringUtils.isEmpty(source.getAddress().getState())) {
					dest.getAddress().setState(source.getAddress().getState());
				}
				if (!StringUtils.isEmpty(source.getAddress().getZip())) {
					dest.getAddress().setZip(source.getAddress().getZip());
				}
				if (!StringUtils.isEmpty(source.getAddress().getCity())) {
					dest.getAddress().setCity(source.getAddress().getCity());
				}
			}
		}

		if (source.getName() != null) {
			if (dest.getName() == null) {
				dest.setName(source.getName());
			} else {
				if (!StringUtils.isEmpty(source.getName().getFirst())) {
					dest.getName().setFirst(source.getName().getFirst());
				}
				if (!StringUtils.isEmpty(source.getName().getMiddle())) {
					dest.getName().setMiddle(source.getName().getMiddle());
				}
				if (!StringUtils.isEmpty(source.getName().getLast())) {
					dest.getName().setLast(source.getName().getLast());
				}
			}
		}

		if (source.getPhone() != null) {
			if (dest.getPhone() == null) {
				dest.setPhone(source.getPhone());
			} else {
				Set<PhoneDto> phone = source.getPhone();
				for (PhoneDto p : phone) {
					PhoneDto oldPhone = getPhoneFromType(dest.getPhone(), p.getType());
					if (oldPhone != null) {
						oldPhone.setNumber(p.getNumber());
					} else {
						dest.getPhone().add(p);
					}

				}
			}
		}

	}

	public PhoneDto getPhoneFromType(Set<PhoneDto> phoneSet, String type) {
		for (PhoneDto p : phoneSet) {
			if (p.getType().equals(type)) {
				return p;
			}
		}
		return null;

	}

	public void validatePhone(ContactDto contactDetails) {

		if (contactDetails != null && contactDetails.getPhone() != null) {
			for (PhoneDto p : contactDetails.getPhone()) {
				if (!ConstantUtils.PHONETYPES.contains(p.getType())) {
					throw new ContactValidationException("Only mobile|home|work allowed for phone type");

				}
			}
		}

	}
}
