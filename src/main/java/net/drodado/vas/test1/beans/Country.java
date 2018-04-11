package net.drodado.vas.test1.beans;

public class Country {

	private String name;
	
	private String dialCode;
	
	private String code;
	
	public Country() {
		super();
	}

	
	public Country(String name, String dialCode, String code) {
		super();
		this.name = name;
		this.dialCode = dialCode;
		this.code = code;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDialCode() {
		return dialCode;
	}

	public void setDialCode(String dialCode) {
		this.dialCode = dialCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((dialCode == null) ? 0 : dialCode.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (dialCode == null) {
			if (other.dialCode != null)
				return false;
		} else if (!dialCode.equals(other.dialCode))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Country [name=" + name + ", dialCode=" + dialCode + ", code=" + code + "]";
	}

}
