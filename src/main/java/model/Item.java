package model;

public class Item
{
    private Product product;
    private float weight;

    public Item(Product product, float weight)
    {
        this.product = product;
        this.weight = weight;
    }

    public float calculatePrice()
    {
        if (weight <= product.getThreshold())
        {
           return product.getPrice() * weight * 2;
        }
        else
        {
            return product.getPrice() * weight;
        }
    }

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public float getWeight()
    {
        return weight;
    }

    public void setWeight(float weight)
    {
        this.weight = weight;
    }
}
