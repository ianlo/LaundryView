package ianlo.net.cmulaundry;

/**
 * Created by ianlo on 2016-01-07.
 */
public class LaundryRoom {
    private String url;
    private String name;
    public LaundryRoom(String name, String url) {
        this.setUrl(url);
        this.setName(name);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static LaundryRoom getRoom(String name) {
        LaundryRoom room;
        switch (name) {
            case "Boss":
                room = RoomConstants.BOSS;
                break;
            case "Doherty":
                room = RoomConstants.DOHERTY;
                break;
            case "Donner":
                room = RoomConstants.DONNER;
                break;
            case "Hamerschlag":
                room = RoomConstants.HAMERSCHLAG;
                break;
            case "Henderson":
                room = RoomConstants.HENDERSON;
                break;
            case "Margaret Morrison 101":
                room = RoomConstants.MARGARET_MORRISON_101;
                break;
            case "Margaret Morrison 102":
                room = RoomConstants.MARGARET_MORRISON_102;
                break;
            case "Margaret Morrison 103":
                room = RoomConstants.MARGARET_MORRISON_103;
                break;
            case "Margaret Morrison 104":
                room = RoomConstants.MARGARET_MORRISON_104;
                break;
            case "Margaret Morrison 105":
                room = RoomConstants.MARGARET_MORRISON_105;
                break;
            case "Margaret Morrison Storefront":
                room = RoomConstants.MARGARET_MORRISON_STOREFRONT;
                break;
            case "Mcgill":
                room = RoomConstants.MCGILL;
                break;
            case "Morewood A":
                room = RoomConstants.MOREWOOD_A;
                break;
            case "Morewood D":
                room = RoomConstants.MOREWOOD_D;
                break;
            case "Morewood E 3rd Floor":
                room = RoomConstants.MOREWOOD_E_3RD_FLR;
                break;
            case "Morewood E 4th Floor":
                room = RoomConstants.MOREWOOD_E_4th_FLR;
                break;
            case "Morewood E 5th Floor":
                room = RoomConstants.MOREWOOD_E_5th_FLR;
                break;
            case "Morewood E 6th Floor":
                room = RoomConstants.MOREWOOD_E_6th_FLR;
                break;
            case "Morewood E 7th Floor":
                room = RoomConstants.MOREWOOD_E_7th_FLR;
                break;
            case "Mudge B":
                room = RoomConstants.MUDGE_B;
                break;
            case "Mudge C":
                room = RoomConstants.MUDGE_C;
                break;
            case "Neville":
                room = RoomConstants.NEVILLE;
                break;
            case "Res on Fifth 4th Floor":
                room = RoomConstants.RES_ON_FIFTH_4TH_FLR;
                break;
            case "Res on Fifth 5th Floor":
                room = RoomConstants.RES_ON_FIFTH_5TH_FLR;
                break;
            case "Resnick":
                room = RoomConstants.RESNICK;
                break;
            case "Scobell":
                room = RoomConstants.SCOBELL;
                break;
            case "Shirley":
                room = RoomConstants.SHIRLEY;
                break;
            case "Spirit House":
                room = RoomConstants.SPIRIT_HOUSE;
                break;
            case "Stever":
                room = RoomConstants.STEVER;
                break;
            case "Welch":
                room = RoomConstants.WELCH;
                break;
            case "Woodlawn":
                room = RoomConstants.WOODLAWN;
                break;
            default:
                room = RoomConstants.STEVER;
                break;
        }
        return room;
    }
}
