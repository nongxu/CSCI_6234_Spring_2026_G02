import { useEffect, useState } from "react";
import { Card, Col, Row, Button, message, Spin } from "antd";
import { useNavigate } from "react-router-dom";

const RESTAURANT_PLACEHOLDER = "https://placehold.co/400x225?text=No+Image";
const MENU_ITEM_PLACEHOLDER = "https://placehold.co/400x400?text=No+Image";

export default function RestaurantPage() {
  const [restaurants, setRestaurants] = useState([]);
  const [menuItems, setMenuItems] = useState([]);
  const [selectedRestaurant, setSelectedRestaurant] = useState(null);
  const [loading, setLoading] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();
  const navigate = useNavigate();

  useEffect(() => {
    fetch("/api/restaurants", { credentials: "include" })
      .then((res) => res.json())
      .then((data) => setRestaurants(data))
      .catch(() => messageApi.error("Failed to load restaurants"));
  }, []);

  const selectRestaurant = async (restaurant) => {
    setSelectedRestaurant(restaurant);
    setLoading(true);
    try {
      const res = await fetch(
        `/api/restaurants/${restaurant.restaurantId}/menu`,
        { credentials: "include" },
      );
      const data = await res.json();
      setMenuItems(data);
    } catch {
      messageApi.error("Failed to load menu");
    } finally {
      setLoading(false);
    }
  };

  const addToCart = async (menuItemId) => {
    const res = await fetch("/api/cart", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify({ menuItemId }),
    });
    if (res.ok) {
      messageApi.success("Added to cart");
    } else if (res.status === 401) {
      messageApi.warning("Please login first");
      navigate("/login");
    } else {
      messageApi.error("Failed to add to cart");
    }
  };

  // Prepend /api so the Vite proxy forwards the path to the backend
  const imageUrl = (path, placeholder) =>
    path ? `/api${path}` : placeholder;

  return (
    <div style={{ padding: 24 }}>
      {contextHolder}
      <h2>Restaurants</h2>
      <Row gutter={[16, 16]}>
        {restaurants.map((r) => (
          <Col key={r.restaurantId} xs={24} sm={12} md={8}>
            <Card
              hoverable
              onClick={() => selectRestaurant(r)}
              style={{
                cursor: "pointer",
                border:
                  selectedRestaurant?.restaurantId === r.restaurantId
                    ? "2px solid #1677ff"
                    : undefined,
              }}
              cover={
                <img
                  alt={r.name}
                  src={imageUrl(r.image, RESTAURANT_PLACEHOLDER)}
                  style={{ height: 180, objectFit: "cover" }}
                />
              }
            >
              <Card.Meta title={r.name} description={r.address} />
              <p style={{ marginTop: 8, color: "#888" }}>{r.phone}</p>
            </Card>
          </Col>
        ))}
      </Row>

      {selectedRestaurant && (
        <div style={{ marginTop: 32 }}>
          <h3>{selectedRestaurant.name} — Menu</h3>
          {loading ? (
            <Spin />
          ) : (
            <Row gutter={[16, 16]}>
              {menuItems.map((item) => (
                <Col key={item.menuItemId} xs={24} sm={12} md={8}>
                  <Card
                    cover={
                      <img
                        alt={item.name}
                        src={imageUrl(item.image, MENU_ITEM_PLACEHOLDER)}
                        style={{ height: 200, objectFit: "cover" }}
                      />
                    }
                  >
                    <Card.Meta
                      title={item.name}
                      description={item.description}
                    />
                    <div
                      style={{
                        marginTop: 12,
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center",
                      }}
                    >
                      <span style={{ fontWeight: 600, fontSize: 16 }}>
                        ${item.price}
                      </span>
                      <Button
                        type="primary"
                        onClick={() => addToCart(item.menuItemId)}
                      >
                        Add to Cart
                      </Button>
                    </div>
                  </Card>
                </Col>
              ))}
            </Row>
          )}
        </div>
      )}

      <div style={{ marginTop: 32 }}>
        <Button onClick={() => navigate("/cart")}>View Cart</Button>
      </div>
    </div>
  );
}
