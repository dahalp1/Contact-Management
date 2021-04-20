package com.mgmt.services;


import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mgmt.dtos.ContactDto;
import com.mgmt.dtos.PhoneDto;
import com.mgmt.exceptions.ContactValidationException;
import com.mgmt.utils.ConstantUtils;

@Service
public class ValidationService {

	public void update(ContactDto src, ContactDto dest) {

		if (!StringUtils.isEmpty(src.getEmail())) {
			dest.setEmail(src.getEmail());
		}

		if (src.getAddress() != null) {
			if (dest.getAddress() == null) {
				dest.setAddress(src.getAddress());
			} else {
				if (!StringUtils.isEmpty(src.getAddress().getStreet())) {
					dest.getAddress().setStreet(src.getAddress().getStreet());
				}
				if (!StringUtils.isEmpty(src.getAddress().getState())) {
					dest.getAddress().setState(src.getAddress().getState());
				}
				if (!StringUtils.isEmpty(src.getAddress().getZip())) {
					dest.getAddress().setZip(src.getAddress().getZip());
				}
				if (!StringUtils.isEmpty(src.getAddress().getCity())) {
					dest.getAddress().setCity(src.getAddress().getCity());
				}
			}
		}

		if (src.getName() != null) {
			if (dest.getName() == null) {
				dest.setName(src.getName());
			} else {
				if (!StringUtils.isEmpty(src.getName().getFirst())) {
					dest.getName().setFirst(src.getName().getFirst());
				}
				if (!StringUtils.isEmpty(src.getName().getMiddle())) {
					dest.getName().setMiddle(src.getName().getMiddle());
				}
				if (!StringUtils.isEmpty(src.getName().getLast())) {
					dest.getName().setLast(src.getName().getLast());
				}
			}
		}

		if (src.getPhone() != null) {
			if (dest.getPhone() == null) {
				dest.setPhone(src.getPhone());
			} else {
				Set<PhoneDto> phone = src.getPhone();
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
