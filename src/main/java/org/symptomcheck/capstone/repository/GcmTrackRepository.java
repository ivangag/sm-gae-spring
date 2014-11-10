package org.symptomcheck.capstone.repository;

import org.springframework.stereotype.Service;

@Service
public class GcmTrackRepository extends JDOCrudRepository<GcmTrack, String>{

	public GcmTrackRepository() {
		super(GcmTrack.class);
	}
}
