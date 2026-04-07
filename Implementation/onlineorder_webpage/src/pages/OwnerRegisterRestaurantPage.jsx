import { useState } from "react";
import { Form, Input, Button, message, Card, Upload } from "antd";
import { PlusOutlined, DeleteOutlined, UploadOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";

// Returns Ant Design Upload props that prevent auto-upload and store the selected file
function imageUploadProps(onFileSelected) {
  return {
    beforeUpload: (file) => {
      const isImage = file.type === "image/jpeg" || file.type === "image/png";
      if (!isImage) {
        message.error("Only JPEG and PNG files are supported.");
        return Upload.LIST_IGNORE;
      }
      onFileSelected(file);
      return false; // prevent auto-upload
    },
    maxCount: 1,
    accept: "image/jpeg,image/png",
  };
}

export default function OwnerRegisterRestaurantPage() {
  const [menuItems, setMenuItems] = useState([
    { name: "", description: "", price: "" },
  ]);
  // Actual File objects for restaurant cover and each menu item
  const [restaurantImageFile, setRestaurantImageFile] = useState(null);
  const [menuItemImageFiles, setMenuItemImageFiles] = useState([null]);

  const [messageApi, contextHolder] = message.useMessage();
  const navigate = useNavigate();
  const [form] = Form.useForm();

  const addMenuItem = () => {
    setMenuItems([...menuItems, { name: "", description: "", price: "" }]);
    setMenuItemImageFiles([...menuItemImageFiles, null]);
  };

  const removeMenuItem = (index) => {
    setMenuItems(menuItems.filter((_, i) => i !== index));
    setMenuItemImageFiles(menuItemImageFiles.filter((_, i) => i !== index));
  };

  const setMenuItemImage = (index, file) => {
    const updated = [...menuItemImageFiles];
    updated[index] = file;
    setMenuItemImageFiles(updated);
  };

  const onFinish = async (values) => {
    // Build JSON text data (images are sent separately as files)
    const textData = {
      restaurantName: values.restaurantName,
      address: values.address,
      phone: values.phone || null,
      menuItems: menuItems.map((_, i) => ({
        name: values[`item_name_${i}`],
        description: values[`item_description_${i}`] || null,
        price: parseFloat(values[`item_price_${i}`]),
      })),
    };

    // Build multipart FormData
    const formData = new FormData();
    formData.append(
      "data",
      new Blob([JSON.stringify(textData)], { type: "application/json" })
    );
    if (restaurantImageFile) {
      formData.append("restaurantImage", restaurantImageFile);
    }
    menuItemImageFiles.forEach((file, i) => {
      if (file) formData.append(`menuItemImage_${i}`, file);
    });

    try {
      const res = await fetch("/api/owner/restaurant", {
        method: "POST",
        credentials: "include",
        body: formData,
        // Do NOT set Content-Type — browser sets it automatically with the correct boundary
      });

      if (res.ok) {
        messageApi.success("Restaurant registered successfully!");
        setTimeout(() => navigate("/owner/login"), 1000);
      } else if (res.status === 401) {
        messageApi.warning("Please login first");
        navigate("/owner/login");
      } else {
        const errorText = await res.text();
        messageApi.error(errorText || "Failed to register restaurant");
      }
    } catch {
      messageApi.error("Network error, please try again");
    }
  };

  return (
    <div style={{ maxWidth: 600, margin: "60px auto", padding: "0 16px" }}>
      {contextHolder}
      <h2>Register Restaurant</h2>
      <Form form={form} layout="vertical" onFinish={onFinish}>

        {/* ── Restaurant Info ───────────────────────────────────────────── */}
        <Form.Item
          label="Restaurant Name"
          name="restaurantName"
          rules={[{ required: true, message: "Please enter restaurant name" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label="Address"
          name="address"
          rules={[{ required: true, message: "Please enter address" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item label="Phone" name="phone">
          <Input />
        </Form.Item>
        <Form.Item label="Restaurant Cover Image (optional)">
          <Upload
            {...imageUploadProps((file) => setRestaurantImageFile(file))}
            fileList={
              restaurantImageFile
                ? [{ uid: "-1", name: restaurantImageFile.name, status: "done" }]
                : []
            }
            onRemove={() => setRestaurantImageFile(null)}
          >
            <Button icon={<UploadOutlined />}>Select Image</Button>
          </Upload>
          <div style={{ color: "#888", fontSize: 12, marginTop: 4 }}>
            JPEG or PNG · min 300×300 px · max 2 MB (auto-compressed if needed)
          </div>
        </Form.Item>

        {/* ── Menu Items ────────────────────────────────────────────────── */}
        <h3>Menu Items</h3>
        {menuItems.map((_, index) => (
          <Card
            key={index}
            style={{ marginBottom: 16 }}
            title={`Item ${index + 1}`}
            extra={
              menuItems.length > 1 && (
                <Button
                  danger
                  icon={<DeleteOutlined />}
                  onClick={() => removeMenuItem(index)}
                />
              )
            }
          >
            <Form.Item
              label="Name"
              name={`item_name_${index}`}
              rules={[{ required: true, message: "Please enter item name" }]}
            >
              <Input />
            </Form.Item>
            <Form.Item label="Description" name={`item_description_${index}`}>
              <Input />
            </Form.Item>
            <Form.Item
              label="Price"
              name={`item_price_${index}`}
              rules={[{ required: true, message: "Please enter price" }]}
            >
              <Input type="number" min="0" step="0.01" />
            </Form.Item>
            <Form.Item label="Item Image (optional)">
              <Upload
                {...imageUploadProps((file) => setMenuItemImage(index, file))}
                fileList={
                  menuItemImageFiles[index]
                    ? [
                        {
                          uid: `-${index}`,
                          name: menuItemImageFiles[index].name,
                          status: "done",
                        },
                      ]
                    : []
                }
                onRemove={() => setMenuItemImage(index, null)}
              >
                <Button icon={<UploadOutlined />}>Select Image</Button>
              </Upload>
              <div style={{ color: "#888", fontSize: 12, marginTop: 4 }}>
                JPEG or PNG · min 300×300 px · max 2 MB (auto-compressed if needed)
              </div>
            </Form.Item>
          </Card>
        ))}

        <Button
          icon={<PlusOutlined />}
          onClick={addMenuItem}
          style={{ marginBottom: 16 }}
        >
          Add Menu Item
        </Button>

        <Form.Item>
          <Button type="primary" htmlType="submit" block>
            Register Restaurant
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
}
