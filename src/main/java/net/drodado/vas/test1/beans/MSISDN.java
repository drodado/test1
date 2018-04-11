package net.drodado.vas.test1.beans;

/**
 * MSISDN Format.
 * 
 * Example:
 * 
 * MSISDN: 919961345678
 * 
 * 	CC	India	91
 * 	NDC	Kerala	9961
 * 	SN	Subscriber's number	345678
 * 
 * @author david
 *
 */
public class MSISDN {

	/**
	 * Country Code.
	 */
	private String cc;
	
	/**
	 * Number Planning Area.
	 */
	private String ndc;
	
	/**
	 * Subscriber Number.
	 */
	private String sn;

	
	
	public MSISDN() {
		super();
	}
	
	
	public MSISDN(String cc, String ndc, String sn) {
		this();
		this.cc = cc;
		this.ndc = ndc;
		this.sn = sn;
	}
	
	
	public MSISDN(String code) {
		this(code.substring(0, 2), code.substring(2, 6), code.substring(6, 12));
	}
	

	public String getCc() {
		return cc;
	}
	

	public String getNdc() {
		return ndc;
	}
	

	public String getSn() {
		return sn;
	}
	

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cc == null) ? 0 : cc.hashCode());
		result = prime * result + ((ndc == null) ? 0 : ndc.hashCode());
		result = prime * result + ((sn == null) ? 0 : sn.hashCode());
		return result;
	}
	

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MSISDN other = (MSISDN) obj;
		if (cc == null) {
			if (other.cc != null)
				return false;
		} else if (!cc.equals(other.cc))
			return false;
		if (ndc == null) {
			if (other.ndc != null)
				return false;
		} else if (!ndc.equals(other.ndc))
			return false;
		if (sn == null) {
			if (other.sn != null)
				return false;
		} else if (!sn.equals(other.sn))
			return false;
		return true;
	}
	

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MSISDN [cc=" + cc + ", ndc=" + ndc + ", sn=" + sn + "]";
	}
	
}
