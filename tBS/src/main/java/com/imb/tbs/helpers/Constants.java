package com.imb.tbs.helpers;

import com.iapps.libs.helpers.BaseConstants;

public class Constants
	extends BaseConstants {
	// ================================================================================
	// Debug purpose
	// ================================================================================
	public static final String	LOG						= "TBS";
	public static final boolean	IS_DEBUGGING			= true;
	public static final String	VERSION					= "24 Aug '15 - Version 3";

	// ================================================================================
	// Pagination Stuff
	// ================================================================================
	public static final int		PAGE_PROFILE			= 0;
	public static final int		PAGE_CAMPAIGN			= 1;
	public static final int		PAGE_REWARDS			= 2;

	// ================================================================================
	// Default Values
	// ================================================================================
	public static final int		DEFAULT_ZOOM_MAPS		= 13;
	public static final int		TIMER_SPLASH			= 3;						// in seconds
	public static final int		LIMIT					= 10;
	public static final int		LIMIT_MIN				= 1;
	public static final String	MALE					= "Male";
	public static final String	FEMALE					= "Female";
	public static final String	PHONE_EXTENSION			= "62";
	public static final int		STAMP_LIMIT				= 500000;
	public static final String	LANG_ID					= "ID";
	public static final String	BASE					= "Base";
	public static final String	VARIANT					= "Variant";
	public static final String	SENDER_ID				= "430001930245";

	// ================================================================================
	// Page Tagging
	// ================================================================================
	public static final int		TAG_REWARDS_MEMBER		= 201;
	public static final int		TAG_REWARDS_BOOK		= 202;
	public static final int		TAG_REWARDS_BDAY		= 203;
	public static final int		TAG_REWARDS_REDEEM		= 204;

	// ================================================================================
	// State
	// ================================================================================
	public static final int		STATE_REWARDS_OK		= 301;
	public static final int		STATE_REWARDS_POINT_REQ	= 302;
	public static final int		STATE_REWARDS_FAN_REQ	= 303;
	public static final int		STATE_REWARDS_CLUB_REQ	= 304;
	public static final int		STATE_REWARDS_FAN		= 2;
	public static final int		STATE_REWARDS_CLUB		= 1;

	// ================================================================================
	// Status
	// ================================================================================
	public static final String	STATUS_LYB				= "LYB Fan";
	public static final String	STATUS_LYB_CLUB			= "LYB Club";
	public static final String	STATUS_STAMP			= "The Body Shop Friend";

	// ================================================================================
	// Object
	// ================================================================================
	public static final String	OBJECT					= "object";

}
