package unlp.info.bd2.model;


import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
@DiscriminatorValue("TOUR_GUIDE")
public class TourGuideUser extends User {

    @Column
    private String education;

    @ManyToMany(mappedBy = "tourGuideList") 
    private List<Route> routes = new ArrayList<>();


    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

}
