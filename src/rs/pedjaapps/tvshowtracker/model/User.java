package rs.pedjaapps.tvshowtracker.model;

/**
 * Created by pedja on 15.6.14..
 */
public class User
{
    private String email;
    private String avatar;
    private String first_name;
    private String last_name;

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getFirst_name()
    {
        return first_name;
    }

    public void setFirst_name(String first_name)
    {
        this.first_name = first_name;
    }

    public String getLast_name()
    {
        return last_name;
    }

    public void setLast_name(String last_name)
    {
        this.last_name = last_name;
    }

    public String getFullName()
    {
        if((first_name == null || "null".equals(first_name)) && (last_name == null|| "null".equals(last_name)))
        {
            return email;
        }
        StringBuilder builder = new StringBuilder();
        if(first_name != null)builder.append(first_name).append(" ");
        if(last_name != null)builder.append(last_name);
        return builder.toString();
    }
}
