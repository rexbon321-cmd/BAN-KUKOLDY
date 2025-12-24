package pl.opban.util;
public class TimeUtil {
 public static long toMillis(int amount, String unit){
  return switch(unit){
   case "MINUTY" -> amount * 60_000L;
   case "GODZINY" -> amount * 3_600_000L;
   case "DNI" -> amount * 86_400_000L;
   case "TYGODNIE" -> amount * 604_800_000L;
   default -> 0;
  };
 }
 public static String format(long ms){
  if(ms<=0) return "PERM";
  long s=ms/1000;
  if(s<60) return s+"s";
  long m=s/60; if(m<60) return m+"m";
  long h=m/60; if(h<24) return h+"h";
  return (h/24)+"d";
 }
}
