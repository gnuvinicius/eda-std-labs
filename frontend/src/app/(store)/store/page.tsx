"use client";

import { CartDrawer } from "@/components/store/CartDrawer";
import { ProductCard } from "@/components/store/ProductCard";
import { CartButton } from "@/components/store/CartButton";

export default function StorePage() {
  const products = [
    { id: "1", name: "Produto A", price: 59.9, description: "Descrição A" },
    { id: "2", name: "Produto B", price: 89.9, description: "Descrição B" },
    { id: "3", name: "Produto C", price: 120.0, description: "Descrição C" },
  ];

  return (
    <CartDrawer>
      {({ cart, setOpen, addToCart }) => (
        <div className="p-6">
          <div className="flex justify-between items-center mb-6">
            <h1 className="text-2xl font-semibold">Loja</h1>
            <CartButton count={cart.length} onClick={() => setOpen(true)} />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {products.map((product) => (
              <ProductCard key={product.id} product={product} addToCart={addToCart} />
            ))}
          </div>
        </div>
      )}
    </CartDrawer>
  );
}
