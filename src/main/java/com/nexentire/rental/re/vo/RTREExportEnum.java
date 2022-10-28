package com.nexentire.rental.re.vo;

import org.apache.commons.lang3.StringUtils;

public class RTREExportEnum {
	
	public enum FileExtType{
		
		TXT("txt"),
        XLS("xls"),
        XLSX("xlsx"),
        CSV("csv"),
        ZIP("zip"),
        UnKnown("")
        ;
		
		private String value = "";
        private FileExtType(String value) {
            this.value = value;
        }
        
        public static FileExtType get(String mode) {
            for ( FileExtType type : FileExtType.values() ) {
                if ( StringUtils.equalsIgnoreCase( type.value, mode ) ) {
                    return type;
                }
            }
            
            return UnKnown;
        }
	}

}
