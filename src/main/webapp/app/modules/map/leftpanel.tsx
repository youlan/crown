import React from 'react';
import {connect} from 'react-redux';
import {LatLng} from './map';
import {Button} from 'reactstrap';
import {AvField, AvForm} from 'availity-reactstrap-validation';

export interface OwnProps {
  position: LatLng,
  radius: number,
  changeRadius: Function,
};
type Props = StateProps & DispatchProps & OwnProps


const img = "content/images/crown.png";


class LeftPanelComponent extends React.Component<Props> {

  toDegreesMinutesAndSeconds = (coordinate)=>{
    const absolute = Math.abs(coordinate);
    const degrees = Math.floor(absolute);
    const minutesNotTruncated = (absolute - degrees) * 60;
    const minutes = Math.floor(minutesNotTruncated);
    const seconds = ((minutesNotTruncated - minutes) * 60).toFixed(1);

    return degrees + "\xB0 " + minutes + "\u2032 " + seconds +"\x22";
  }

  convertDMS = (lat, lng) => {
    const latitude = this.toDegreesMinutesAndSeconds(lat);
    const latitudeCardinal = lat >= 0 ? "N" : "S";

    const longitude = this.toDegreesMinutesAndSeconds(lng);
    const longitudeCardinal = lng >= 0 ? "E" : "W";

    return {
      DMSlat: latitude + " " + latitudeCardinal,
      DMSlng: longitude + " " + longitudeCardinal,
    };
  }


  render(){
    const {position: {lat, lng}, radius, changeRadius} = this.props;
    const {DMSlat, DMSlng} = this.convertDMS(lat, lng);

    return (
      <div className="left-panel shadow-lg p-3 mb-5 bg-blue rounded">
        <img src={img} style={{borderTopLeftRadius:'10px',borderTopRightRadius:'10px'}}/>
        <div className="align-content-center">
          <div className="align-content-center crown-header" style={{padding:'5px',color:'#fff'}}>A COVID-19 Open Data Network for Rapid, Affordable and Standardized Procurement of Manufactured Medical Supplies (MS)</div>
        </div>
        <div className="align-content-center">
          <div className="align-content-center crown-header-white">LOCATION</div>
        </div>

        <div className="radius-info-div">
          <p className="latlong">{DMSlat}</p>
          <p className="latlong">{DMSlng}</p>
          <div className="align-content-center">
            <div className="align-content-center crown-header-white">RADIUS (KM)</div>
          </div>

          <AvForm className="radius-submit" onSubmit={changeRadius}>
            <AvField
              name="radius"
              type="number"
              placeholder="Enter Radius"
              required
              value={radius}
            />
            <Button className="radius-button" type="submit"> Change </Button>
          </AvForm>
        </div>
      </div>
    )
  }
}

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated
});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps)(LeftPanelComponent);
