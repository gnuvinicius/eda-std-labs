"use client";

import { Card, CardHeader, CardContent, CardFooter } from "@/components/ui/card";
import { Button } from "@/components/ui/button";

export function ProductCard({ product, addToCart }: any) {
  return (
    <Card className="w-full">
      <CardHeader>
        <h2 className="font-semibold">{product.name}</h2>
      </CardHeader>

      <CardContent>
        <p className="text-gray-600">{product.description}</p>
        <p className="mt-2 font-bold">R$ {product.price}</p>
      </CardContent>

      <CardFooter>
        <Button className="w-full" onClick={() => addToCart(product)}>
          Adicionar ao Carrinho
        </Button>
      </CardFooter>
    </Card>
  );
}
