package com.dayatang.domain;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public enum DataType {
	
	STRING {
		@Override
		public String getDefaultValue() {
			return "";
		}
	},
	
	INT {
		@Override
		public Integer getDefaultValue() {
			return 0;
		}
	},
	
	DOUBLE {
		@Override
		public Double getDefaultValue() {
			return 0.0;
		}
	},
	
	BIG_DECIMAL {
		@Override
		public BigDecimal getDefaultValue() {
			return BigDecimal.ZERO;
		}
	},
	
	BOOLEAN {
		@Override
		public Boolean getDefaultValue() {
			return false;
		}
	},
	
	DATE {
		@Override
		public Date getDefaultValue() {
			try {
				return DateUtils.parseDate("1970-01-01", new String[] {"yyyy-MM-dd"});
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
	},
	
	TIME {
		@Override
		public Date getDefaultValue() {
			try {
				return DateUtils.parseDate("00:00:00", new String[] {"hh:mm:ss"});
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
	},
	
	DATE_TIME {
		@Override
		public Date getDefaultValue() {
			try {
				return DateUtils.parseDate("1970-01-01 00:00:00", new String[] {"yyyy-MM-dd hh:mm:ss"});
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
	};

	public abstract Object getDefaultValue();
	
}
