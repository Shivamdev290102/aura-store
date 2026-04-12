import API from "./axios";

// Update shipment
export const updateShipment = (shipmentId, data) => {
  return API.put(`/shipments/${shipmentId}`, data);
};