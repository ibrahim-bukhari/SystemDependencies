import java.util.Map;

public class Component {
	private String name;
	private boolean explicitInstall;
	
	
	Component(String name, boolean explicitInstall) {
		setName(name);
		setExplicitInstall(explicitInstall);
	}
	
	public String getName() {
		return this.name;
	}
	private void setName(String name) {
		this.name = name;
	}
	
	public boolean getExplicitInstall() {
		return this.explicitInstall;
	}
	public void setExplicitInstall(boolean explicitInstall) {
		this.explicitInstall = explicitInstall;
	}
}
